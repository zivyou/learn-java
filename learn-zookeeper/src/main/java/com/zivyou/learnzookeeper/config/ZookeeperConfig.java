package com.zivyou.learnzookeeper.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConfig {
    private final ZooKeeperProperty zooKeeperProperty;

    public ZookeeperConfig(ZooKeeperProperty zooKeeperProperty) {
        this.zooKeeperProperty = zooKeeperProperty;
    }

    @Bean
    public CuratorFramework curatorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zooKeeperProperty.getBaseSleepTime(),
                zooKeeperProperty.getMaxRetries());
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zooKeeperProperty.getAddress())
                .connectionTimeoutMs(zooKeeperProperty.getConnectionTimeout())
                .sessionTimeoutMs(zooKeeperProperty.getSessionTimeout())
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        return client;
    }
}
