package com.zivyou.learnpaxos.service;

import com.zivyou.learnpaxos.constant.MqttTopics;
import com.zivyou.learnpaxos.db.DataMap;
import com.zivyou.learnpaxos.entity.Proposal;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class Learner {
    @Resource
    private final MqttClient mqttSubscribeClient;
    private final DataMap dataMap;

    // 记录每个key的已chosen状态，避免重复处理
    private final Map<String, String> chosenValues = new ConcurrentHashMap<>();

    // 记录每个key收到的accepted value及其计数
    // 结构: key -> (value -> count)
    private final Map<String, Map<String, Integer>> acceptedCounts = new ConcurrentHashMap<>();

    @Value("${paxos.acceptor-amount}")
    private Integer acceptorAmount;

    @PostConstruct
    void init() throws MqttException {
        log.info("Learner: init..................... mqttSubscribeClient: {}",
                mqttSubscribeClient.isConnected());
        // 订阅Acceptor的Accepted响应
        mqttSubscribeClient.subscribeWithResponse(MqttTopics.ACCEPTOR_ACCEPTED, 0, onAccepted());
        log.info("Learner: end.....................");
    }

    private IMqttMessageListener onAccepted() {
        return (topic, message) -> {
            if (!topic.equals(MqttTopics.ACCEPTOR_ACCEPTED)) return;

            try {
                Proposal proposal = Proposal.fromMessage(new String(message.getPayload()));
                handleAccepted(proposal);
            } catch (Exception e) {
                log.error("Learner: handleAccepted failed", e);
            }
        };
    }

    private void handleAccepted(Proposal proposal) {
        String key = proposal.getKey();
        String value = proposal.getValue();

        // 如果这个key已经被chosen，忽略后续的accepted消息
        if (chosenValues.containsKey(key)) {
            log.debug("Learner: key {} already chosen, value: {}, ignoring accepted: {}",
                key, chosenValues.get(key), value);
            return;
        }

        log.info("Learner: received accepted - key: {}, value: {}, requestId: {}",
            key, value, proposal.getRequestId());

        // 记录收到的accepted value
        acceptedCounts.computeIfAbsent(key, k -> new ConcurrentHashMap<>())
            .merge(value, 1, Integer::sum);

        // 检查是否有value获得多数派
        Map<String, Integer> valueCounts = acceptedCounts.get(key);
        for (Map.Entry<String, Integer> entry : valueCounts.entrySet()) {
            if (entry.getValue() > acceptorAmount / 2) {
                // 这个value被chosen了
                String chosenValue = entry.getKey();
                log.info("Learner: value chosen - key: {}, value: {}, count: {}, majority: {}",
                    key, chosenValue, entry.getValue(), acceptorAmount / 2);

                // 记录chosen状态
                chosenValues.put(key, chosenValue);

                // 应用到DataMap
                dataMap.put(key, chosenValue);

                // 清理计数
                acceptedCounts.remove(key);
                break;
            }
        }
    }

    public void learn(Proposal proposal) {
        // 这个方法保留供外部调用，实际学习逻辑在onAccepted中处理
        log.info("Learner: learn called - key: {}, value: {}", proposal.getKey(), proposal.getValue());
    }
}
