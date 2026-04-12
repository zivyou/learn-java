package com.zivyou.learnkafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {
    @KafkaListener(topics = "yzq-test-topic", groupId = "yzq-test-group")
    public void consumer(String message) {
        log.info("received message from kafka : {}", message);
    }
}
