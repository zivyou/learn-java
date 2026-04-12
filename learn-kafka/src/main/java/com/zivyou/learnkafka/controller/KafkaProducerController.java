package com.zivyou.learnkafka.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/test")
public class KafkaProducerController {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/send-message")
    public void sendKafkaMessage(@RequestBody String message) {
        kafkaTemplate.send("yzq-test-topic", message);
    }
}
