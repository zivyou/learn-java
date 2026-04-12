package com.zivyou.learnzookeeper.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zookeeper")
@Getter
public class ZooKeeperProperty {
    private String address;
    private int sessionTimeout = 60000;
    private int connectionTimeout = 15000;
    private int baseSleepTime = 1000;
    private int maxRetries = 10;
}
