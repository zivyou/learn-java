package com.zivyou.learnspringredis;

import com.zivyou.learnspringredis.pojo.Book;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class LearnSpringRedisApplicationTests {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    public void test01() {
        String key = "test01";
        var valueOps = redisTemplate.boundValueOps(key);
        valueOps.set(new Book("123", "abc"), 10, TimeUnit.MINUTES);
        log.info("valueOps: success");
    }

}
