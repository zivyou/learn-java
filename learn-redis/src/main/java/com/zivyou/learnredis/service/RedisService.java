package com.zivyou.learnredis.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisMessageListenerContainer container;
    private final MessageListener messageListener;
    private final StreamListener<String,MapRecord<String,String,String>> streamListener;
    private final StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer;

    public void subscribe(String topic) {
        container.addMessageListener(messageListener, new ChannelTopic(topic));
    }

    public void subscribe(String topic, MessageListener consumer) {
        container.addMessageListener(consumer, new ChannelTopic(topic));
    }

    public void publish(String topic, String message) {
        redisTemplate.convertAndSend(topic, message);
    }

    public void publishStreamMessage(String key, String message) {
        redisTemplate.opsForStream().add(MapRecord.create(key, new HashMap<>(){{
            put("hello", "world!");
        }}));
    }

    public void subscribeStream(String streamKey, String group, String subscriberName) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(streamKey))) {
            StreamInfo.XInfoGroups groups = redisTemplate.opsForStream().groups(streamKey);
            if (groups.isEmpty()) {
                redisTemplate.opsForStream().createGroup(streamKey, group);
            } else {
                log.info("--------------------------------> {}", groups);
            }
        } else {
            redisTemplate.opsForStream().createGroup(streamKey, group);
        }
        streamMessageListenerContainer.receiveAutoAck( Consumer.from(group, subscriberName),
                StreamOffset.create(streamKey, ReadOffset.lastConsumed()), streamListener);
    }

    public boolean lock(String key) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofSeconds(10)));
    }

    public boolean unlock(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @PostConstruct
    void init() {
        subscribe("yzq:test:pub-sub-channel");
        subscribeStream("yzq:test:stream-key", "group0", "dummy-stream-subscriber");
    }
}
