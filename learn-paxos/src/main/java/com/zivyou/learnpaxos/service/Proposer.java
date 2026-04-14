package com.zivyou.learnpaxos.service;

import com.zivyou.learnpaxos.constant.MqttTopics;
import com.zivyou.learnpaxos.entity.Proposal;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class Proposer {
    @Resource
    private final MqttClient mqttPublishClient;
    @Resource
    private final MqttClient mqttSubscribeClient;
    private final Map<String, Proposal> currentProposals = new ConcurrentHashMap<>();
    private final Map<String, Integer> currentProposalPrepareResponded = new ConcurrentHashMap<>();
    private final Map<String, Integer> currentProposalAcceptResponded = new ConcurrentHashMap<>();
    private final Map<String, Integer> currentProposalRejectResponded = new ConcurrentHashMap<>();
    private final Map<String, Proposal> acceptedProposals = new ConcurrentHashMap<>();
    @Value("${paxos.acceptor-amount}")
    private Integer acceptorAmount;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @PostConstruct
    void init() throws MqttException {
        log.info("Proposer: init..................... mqttPublishClient: {}, mqttSubscribeClient: {}",
                mqttPublishClient.isConnected(), mqttSubscribeClient.isConnected());
        mqttSubscribeClient.subscribeWithResponse(MqttTopics.PROPOSAL_PREPARE_RESPONSE, 0, onPrepareResponse());
        mqttSubscribeClient.subscribeWithResponse(MqttTopics.PROPOSAL_ACCEPT_RESPONSE, 0, onAcceptResponse());
        log.info("Proposer: end.....................");
    }

    private IMqttMessageListener onPrepareResponse() {
        return (topic, message) -> {
            if (!topic.equals(MqttTopics.PROPOSAL_PREPARE_RESPONSE)) return;
            Proposal proposal = Proposal.fromMessage(new String(message.getPayload()));
            log.info("onPrepareResponse: currentProposals: {}", currentProposals);
            if (!currentProposals.containsKey(proposal.getKey())){
                log.info("onPrepareResponse: currentProposals does not contains {}", proposal.getKey());
                return;
            }
            currentProposalPrepareResponded.put(proposal.getKey(), currentProposalPrepareResponded.getOrDefault(proposal.getKey(),0)+1);
            log.info("onPrepareResponse: currentProposalPrepareRespondedCount: {}", currentProposalPrepareResponded.get(proposal.getKey()));
            if (proposal.getResponseId() != null && proposal.getResponseId() > currentProposals.get(proposal.getKey()).getRequestId()) {
                // responseId > requestId: 说明有其他proposer发布了更早的提案，我们应该放弃当前的提案，重新prepare一次；
                var key=proposal.getKey(); var value=proposal.getValue();
                currentProposals.remove(proposal.getKey());
                currentProposalPrepareResponded.remove(proposal.getKey());
                propose(key,value);
                return;
            }
            if (currentProposalPrepareResponded.get(proposal.getKey())*2 > acceptorAmount) {
                publishAcceptRequest(currentProposals.get(proposal.getKey()));
            }
        };
    }

    private void publishAcceptRequest(Proposal proposal) {
        String data = proposal.toString();
        byte[] message = data.getBytes();
        log.info("Proposer: publishAcceptRequest {}", proposal);
        executorService.execute(() -> {
            try {
                mqttPublishClient.publish(MqttTopics.PROPOSAL_ACCEPT_REQUEST, message, 0, false);
            } catch (MqttException e) {
                log.error("Proposer: publishAcceptRequest failed", e);
            }
        });
    }

    private IMqttMessageListener onAcceptResponse() {
        return (topic, message) -> {
            if (!topic.equals(MqttTopics.PROPOSAL_ACCEPT_RESPONSE)) return;
            Proposal proposal = Proposal.fromMessage(new String(message.getPayload()));
            if (!currentProposals.containsKey(proposal.getKey())) {
                log.info("Proposer: onAcceptResponse: currentProposals are {}, does not contain {}", currentProposals, proposal);
                return;
            };
            log.info("Proposer: onAcceptResponse {}", proposal);
            if (proposal.getRequestId() > currentProposals.get(proposal.getKey()).getRequestId()) {
                currentProposalAcceptResponded.put(proposal.getKey(), currentProposalAcceptResponded.getOrDefault(proposal.getKey(), 0) + 1);
            } else {
                currentProposalRejectResponded.put(proposal.getKey(), currentProposalRejectResponded.getOrDefault(proposal.getKey(), 0) + 1);
            }

            var acceptedAmount = currentProposalAcceptResponded.get(proposal.getKey());
            var rejectedAmount = currentProposalRejectResponded.get(proposal.getKey());
            if ((acceptedAmount+rejectedAmount) >= acceptorAmount) {
                if (acceptedAmount > acceptorAmount / 2) {
                    // value is chosen
                    log.info("Proposer: value is chosen: {}", currentProposals.get(proposal.getKey()).getValue());
                    currentProposals.remove(proposal.getKey());
                } else {
                    // value is not chosen
                    log.info("Proposer: value is not chosen: {}", currentProposals.get(proposal.getKey()).getValue());
//                    propose(proposal.getKey(), currentProposals.get(proposal.getKey()).getValue());
                }
            }
        };
    }

    private void publishPrepareRequest(Proposal proposal) {
        String data = proposal.toString();
        byte[] message = data.getBytes();
        executorService.execute(() -> {
            try {
                log.info("publishPrepareRequest: {}", proposal);
                mqttPublishClient.publish(MqttTopics.PROPOSAL_PREPARE_REQUEST, message, 0, false);
                log.info("publishPrepareRequest: success");
            } catch (MqttException e) {
                log.error("Proposer: publishPrepareRequest failed", e);
            }
        });
    }

    /**
     *
     * @param key
     * @param value
     * 提议者在提交一个proposal的时候，需要注意到: 1. 自己需要提供一个proposalId即可，在prepare阶段大家都是抢一个proposalId;
     * 2. 回应的人，可以并不一定会认可自己提出的ProposalId，说白了就是婉拒。被婉拒了之后自己也得认;
     */

    public void propose(String key, String value) {
        currentProposalPrepareResponded.put(key, 0);
        var proposalId = System.currentTimeMillis();
        var originProposal = new Proposal(proposalId, key, value, null);
        currentProposals.put(key, originProposal);
        publishPrepareRequest(originProposal);
    }
}
