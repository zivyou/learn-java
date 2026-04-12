package com.zivyou.learnredis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class MyStreamConsumer implements StreamListener<String, MapRecord<String, String, String>> {
    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        Map<String, String> content = message.getValue();
        log.info("================> MyStreamConsumer.onMessage: message: {}", content);
        log.info("======================> ????????????????? {}", content.keySet());
    }
}
