package com.zivyou.learnpaxos.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.UUID;

@Configuration
@ConfigurationProperties(prefix = "mqtt")
@Setter
@Slf4j
public class MqttConnectionConfig {
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
            log.error("init mqttPublishClient failed", e);
        }
        var options = new MqttConnectOptions();
        options.setUserName(userName); options.setPassword(password.toCharArray());
        try {
            if (client != null) {
                client.connect(options);
            } else {
                throw new RuntimeException("client is null");
            }
        } catch (MqttException e) {
            log.error("mqttPublishClient connect failed", e);
        }
        return client;
    }

    @Bean
    public MqttClient mqttSubscribeClient() {
        MqttClient client = null;
        try {
            client = new MqttClient(broker, UUID.randomUUID().toString());
        } catch (MqttException e) {
            log.error("init mqttSubscribeClient failed", e);
        }
        var options = new MqttConnectOptions();
        options.setUserName(userName); options.setPassword(password.toCharArray());

        try {
            if (client != null) {
                client.connect(options);
            } else {
                throw new RuntimeException("client is null");
            }
        } catch (MqttException e) {
            log.error("mqttSubscribeClient connect failed", e);
        }
        return client;
    }

    @PostConstruct
    void init() {
        log.info("=============> {}, {}, {}, {}", broker, userName, password, qos);
    }
}
