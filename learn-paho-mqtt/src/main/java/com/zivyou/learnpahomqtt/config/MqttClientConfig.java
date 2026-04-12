package com.zivyou.learnpahomqtt.config;

import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@ConfigurationProperties(prefix = "mqtt")
@Slf4j
@Setter
public class MqttClientConfig {
    private String broker;
    private String userName;
    private String password;
    private Integer qos;
    @Bean
    public MqttClient mqttPublishClient() {
        MqttClient client = null;
        try {
            client = new MqttClient(broker, UUID.randomUUID().toString());
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        var options = new MqttConnectOptions();
        options.setUserName(userName); options.setPassword(password.toCharArray());
        try {
            client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return client;
    }

    @Bean
    public MqttClient mqttSubscribeClient() {
        MqttClient client = null;
        try {
            client = new MqttClient(broker, UUID.randomUUID().toString());
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        var options = new MqttConnectOptions();
        options.setUserName(userName); options.setPassword(password.toCharArray());
        try {
            client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return client;
    }

    @PostConstruct
    void init() {
        log.info("=============> {}, {}, {}, {}", broker, userName, password, qos);
    }
}
