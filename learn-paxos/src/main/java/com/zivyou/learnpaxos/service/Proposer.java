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
import java.util.concurrent.*;
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
    // 存储每个key的Accept超时任务，用于在获得多数派时取消超时
    private final Map<String, ScheduledFuture<?>> acceptTimeoutTasks = new ConcurrentHashMap<>();
    @Value("${paxos.acceptor-amount}")
    private Integer acceptorAmount;
    @Value("${paxos.accept-timeout-ms:5000}")
    private Long acceptTimeoutMs;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

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

            // 计数收到的响应
            currentProposalPrepareResponded.put(proposal.getKey(), currentProposalPrepareResponded.getOrDefault(proposal.getKey(),0)+1);
            log.info("onPrepareResponse: currentProposalPrepareRespondedCount: {}", currentProposalPrepareResponded.get(proposal.getKey()));

            // 记录Acceptor返回的已接受value（如果有）
            // 如果responseId != null，说明Acceptor之前已accept过某个value
            if (proposal.getResponseId() != null) {
                Proposal currentAccepted = acceptedProposals.get(proposal.getKey());
                // 只记录最大的responseId对应的value
                if (currentAccepted == null || proposal.getResponseId() > currentAccepted.getRequestId()) {
                    acceptedProposals.put(proposal.getKey(), proposal);
                    log.info("onPrepareResponse: 记录已接受的value - key: {}, value: {}, responseId: {}",
                        proposal.getKey(), proposal.getValue(), proposal.getResponseId());
                }
            }

            // 注意：Basic Paxos中，Prepare阶段不需要检测冲突
            // responseId > requestId 只是说明Acceptor之前accept过value，不是冲突
            // 真正的冲突会在Accept阶段体现（Accept被拒绝）

            // 收到多数派响应，进入Accept阶段
            if (currentProposalPrepareResponded.get(proposal.getKey())*2 > acceptorAmount) {
                // 选择要accept的value：如果有已接受的value，使用它；否则使用原始value
                String valueToAccept;
                if (acceptedProposals.containsKey(proposal.getKey())) {
                    valueToAccept = acceptedProposals.get(proposal.getKey()).getValue();
                    log.info("onPrepareResponse: 使用已接受的value - key: {}, value: {}", proposal.getKey(), valueToAccept);
                } else {
                    valueToAccept = currentProposals.get(proposal.getKey()).getValue();
                    log.info("onPrepareResponse: 使用原始value - key: {}, value: {}", proposal.getKey(), valueToAccept);
                }
                // 创建新的proposal用于accept，使用正确的value
                var acceptProposal = new Proposal(
                    currentProposals.get(proposal.getKey()).getRequestId(),
                    proposal.getKey(),
                    valueToAccept,
                    null
                );
                publishAcceptRequest(acceptProposal);
            }
        };
    }

    private void publishAcceptRequest(Proposal proposal) {
        String key = proposal.getKey();
        String data = proposal.toString();
        byte[] message = data.getBytes();
        log.info("Proposer: publishAcceptRequest {}", proposal);

        executorService.execute(() -> {
            try {
                mqttPublishClient.publish(MqttTopics.PROPOSAL_ACCEPT_REQUEST, message, 0, false);

                // 设置超时定时器
                ScheduledFuture<?> timeoutTask = scheduledExecutorService.schedule(() -> {
                    log.info("Proposer: accept timeout - key: {}, requestId: {}", key, proposal.getRequestId());

                    // 检查是否已经获得多数派（可能竞态）
                    Integer acceptedAmount = currentProposalAcceptResponded.get(key);
                    if (acceptedAmount != null && acceptedAmount > acceptorAmount / 2) {
                        // 已经获得多数派，超时任务被延迟执行，忽略
                        log.debug("Proposer: timeout triggered but value already chosen - key: {}", key);
                        return;
                    }

                    // 未获得多数派，重新提议
                    if (currentProposals.containsKey(key)) {
                        String value = currentProposals.get(key).getValue();
                        Long oldRequestId = currentProposals.get(key).getRequestId();

                        log.info("Proposer: retrying after timeout - key: {}, oldRequestId: {}, value: {}", key, oldRequestId, value);

                        // 清理当前状态
                        currentProposals.remove(key);
                        currentProposalAcceptResponded.remove(key);
                        currentProposalPrepareResponded.remove(key);
                        acceptedProposals.remove(key);
                        acceptTimeoutTasks.remove(key);

                        // 用更大的number重新提议
                        propose(key, value);
                    } else {
                        // 可能已经获得多数派并被清理了，忽略
                        log.debug("Proposer: timeout triggered but proposal already cleaned - key: {}", key);
                        acceptTimeoutTasks.remove(key);
                    }
                }, acceptTimeoutMs, TimeUnit.MILLISECONDS);

                // 存储超时任务，以便在获得多数派时取消
                acceptTimeoutTasks.put(key, timeoutTask);

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

            // 检查收到的响应是否与当前proposal匹配
            if (proposal.getRequestId().equals(currentProposals.get(proposal.getKey()).getRequestId())) {
                // 收到的响应与当前proposal匹配，计为accept
                currentProposalAcceptResponded.put(proposal.getKey(), currentProposalAcceptResponded.getOrDefault(proposal.getKey(), 0) + 1);

                var acceptedAmount = currentProposalAcceptResponded.get(proposal.getKey());
                log.info("Proposer: accept count - key: {}, accepted: {}, majority: {}",
                    proposal.getKey(), acceptedAmount, acceptorAmount / 2);

                // 如果收到多数派accept，value is chosen
                if (acceptedAmount > acceptorAmount / 2) {
                    String key = proposal.getKey();
                    String value = currentProposals.get(key).getValue();
                    log.info("Proposer: value is chosen - key: {}, value: {}, accepted: {}",
                        key, value, acceptedAmount);

                    // 取消超时任务
                    ScheduledFuture<?> timeoutTask = acceptTimeoutTasks.remove(key);
                    if (timeoutTask != null) {
                        timeoutTask.cancel(false);
                        log.debug("Proposer: cancelled accept timeout task - key: {}", key);
                    }

                    // 清理所有状态
                    currentProposals.remove(key);
                    currentProposalAcceptResponded.remove(key);
                    currentProposalPrepareResponded.remove(key);
                    acceptedProposals.remove(key);
                    // currentProposalRejectResponded 不再使用，但保留以避免潜在的NPE
                }
                // 注意：如果未获得多数派accept，Proposer等待超时
                // 超时逻辑需要额外实现（使用ScheduledExecutorService）
            } else {
                // 收到的响应与当前proposal不匹配（可能是更大的number）
                // 说明有冲突，当前proposal可能已经无效
                log.info("Proposer: received mismatched response - key: {}, currentRequestId: {}, receivedRequestId: {}",
                    proposal.getKey(), currentProposals.get(proposal.getKey()).getRequestId(), proposal.getRequestId());
                // 可以在这里触发重新提议，但更可靠的方式是等待超时
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
