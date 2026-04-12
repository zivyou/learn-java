package com.zivyou.learnspring;

import com.zivyou.learnspring.event.DummyEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootTest
class LearnSpringApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;
    @Value("${myconfig}")
    private List<String> redisNodes;
    @Test
    void contextLoads() {
    }

    @Test
    public void test01() {
        applicationContext.publishEvent(new DummyEvent("hello?"));
    }

    @Test
    public void test02() {
        System.out.println(redisNodes);
    }

}
