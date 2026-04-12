package com.zivyou.learnpahomqtt.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MqttListener {
    @Resource
    private final MqttClient mqttSubscribeClient;
    @PostConstruct
    void init() {
        try {
            mqttSubscribeClient.subscribe("learn-paho-mqtt-sub",0, onMessageArrived());
        } catch (MqttException e) {
            log.error("MqttListener subscribe error", e);
        }
    }

    private IMqttMessageListener onMessageArrived() {
        return (topic, message) -> {
            log.info("received message {} from topic {}", topic, new String(message.getPayload()));
        };
    }
}
