package com.zivyou.learnredis;

import com.zivyou.learnredis.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTests {
    @Autowired
    RedisService redisService;

    @Test
    public void test01() {
        redisService.publish("yzq:test:pub-sub-channel", "hello world!");
    }
    @Test
    public void test02() {
        redisService.publishStreamMessage("yzq:test:stream-key", "hello world!");
    }
}
