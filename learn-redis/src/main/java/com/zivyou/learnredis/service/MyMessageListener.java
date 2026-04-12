package com.zivyou.learnredis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel =  new String(message.getChannel());
        String content = new String(message.getBody());
        log.info("myRedisListener.onMessage: topic: {}, content: {}",
              channel, content);
    }
}
