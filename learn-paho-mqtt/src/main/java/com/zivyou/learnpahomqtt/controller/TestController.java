package com.zivyou.learnpahomqtt.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {
    @Resource
    private MqttClient mqttPublishClient;
    @GetMapping("/pub")
    public String pub(@RequestParam String message){
        try {
            mqttPublishClient.publish("learn-paho-mqtt-sub", message.getBytes(),0,false);
        } catch (MqttException e) {
            log.error("mqtt pub message failed.", e);
        }
        return "Message published successfully";
    }
}
