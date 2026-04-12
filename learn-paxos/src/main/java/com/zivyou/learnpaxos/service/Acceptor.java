package com.zivyou.learnpaxos.service;

import com.zivyou.learnpaxos.constant.MqttTopics;
import com.zivyou.learnpaxos.db.DataMap;
import com.zivyou.learnpaxos.entity.Proposal;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class Acceptor {
    @Resource
    private final MqttClient mqttPublishClient;
    @Resource
    private final MqttClient mqttSubscribeClient;
    private final DataMap dataMap;
    private final Map<String, Proposal> acceptedProposals = new ConcurrentHashMap<>();
    private final Map<String, Proposal> maxProposals = new ConcurrentHashMap<>();
    private final List<Learner> learners;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @PostConstruct
    void init() {
        try {
            mqttSubscribeClient.subscribeWithResponse(MqttTopics.PROPOSAL_PREPARE_REQUEST, 0, onPrepareRequest());
            mqttSubscribeClient.subscribeWithResponse(MqttTopics.PROPOSAL_ACCEPT_REQUEST, 0, onAcceptRequest());
        } catch (MqttException e) {
            log.error("Acceptor mqttSubscribeClient failed", e);
        }
    }

    private IMqttMessageListener onAcceptRequest() {
        return (topic, message) -> {
            if (!topic.equals(MqttTopics.PROPOSAL_ACCEPT_REQUEST)) return;
            Proposal proposal = Proposal.fromMessage(new String(message.getPayload()));
            log.info("Acceptor: onAcceptRequest: {}", proposal);
            receiveAcceptRequest(proposal);
        };
    }

    private void responsePrepare(Proposal proposal) {
        var message = proposal.toString();
        executorService.execute(() -> {
            try {
                log.info("Acceptor: responsePrepare: {}", proposal);
                mqttPublishClient.publish(MqttTopics.PROPOSAL_PREPARE_RESPONSE, message.getBytes(), 0, false);
            } catch (MqttException e) {
                log.error("Acceptor: responsePrepare failed", e);
            }
        });
    }

    private IMqttMessageListener onPrepareRequest() {
        return (topic, message) -> {
            try {
                log.info("Acceptor: onPrepareRequest-----------------------------> topic: {}", topic);
                if (!topic.equals(MqttTopics.PROPOSAL_PREPARE_REQUEST)) return;
                byte[] payload = message.getPayload();
                String data = new String(payload);
                log.info("Acceptor: onPrepareRequest: {}", data);
                Proposal proposal = Proposal.fromMessage(data);
                receivePrepare(proposal);
            } catch (Exception e) {
                log.error("onPrepareRequest: ", e);
            }
        };
    }

    private void receivePrepare(Proposal proposal) {
        log.info("Acceptor: receivePrepare: {}", proposal);
        if (!maxProposals.containsKey(proposal.getKey()) || maxProposals.get(proposal.getKey()).getRequestId() < proposal.getRequestId()) {
            maxProposals.put(proposal.getKey(), proposal);
            if (acceptedProposals.containsKey(proposal.getKey())) {
                responsePrepare(
                        new Proposal(
                                proposal.getRequestId(), proposal.getKey(),
                            proposal.getValue(), acceptedProposals.get(proposal.getKey()).getRequestId()
                        )
                );
            } else {
                proposal.setResponseId(proposal.getRequestId());
                responsePrepare(proposal);
            }
        } else {
            // do nothing
        }
    }

    private void responseAccept(Proposal proposal) {
        var message = proposal.toString();
        executorService.execute(() -> {
            try {
                mqttPublishClient.publish(MqttTopics.PROPOSAL_ACCEPT_RESPONSE, message.getBytes(), 0, false);
            } catch (MqttException e) {
                log.error("Acceptor: responseAccept failed", e);
            }
        });
    }

    public void receiveAcceptRequest(Proposal proposal) {
        log.info("Acceptor: receiveAccept: {}", proposal);
        if (proposal.getRequestId() < maxProposals.get(proposal.getKey()).getRequestId()) {

          return;
        } else {
            acceptedProposals.put(proposal.getKey(), proposal);
            log.info("??????????????????????????????????????????????????????????????????");
            responseAccept(proposal);
            // 记录下最终数值，并通知listener;
            dataMap.put(proposal.getKey(), proposal.getValue());
        }
    }
}
