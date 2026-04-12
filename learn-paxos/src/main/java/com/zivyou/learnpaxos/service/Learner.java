package com.zivyou.learnpaxos.service;

import com.zivyou.learnpaxos.constant.MqttTopics;
import com.zivyou.learnpaxos.entity.Proposal;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Learner {
    private final Map<String, Integer> counts = new HashMap<>();
    @Resource
    private final MqttClient mqttSubscribeClient;

    @PostConstruct
    void init() throws MqttException {
//        mqttSubscribeClient.subscribeWithResponse(MqttTopics.LEARN_REQUEST, 0, onLearnRequest());
    }

    private IMqttMessageListener onLearnRequest() {
        return (topic, message) -> {
            if (!topic.equals(MqttTopics.LEARN_REQUEST)) return;

        };
    }

//    private final List<Acceptor> acceptors;
    public void learn(Proposal proposal) {
        /*
        int originCount = Optional.of(counts).map(c -> c.get(proposal.getValue())).orElse(0);
        counts.put(proposal.getValue(), counts.get(proposal.getValue())+1);
        int acceptorAmount = acceptors.size();
        for (var key : counts.keySet()) {
            int count = counts.get(key);
            if (2*count + 1 > acceptorAmount) {
                // choose this value
                return;
            }
        }
         */
    }
}
