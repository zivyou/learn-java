package com.zivyou.learnspring.springextension;

import com.zivyou.learnspring.service.GuardedObjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MySpringTests {
    @Autowired
    private GuardedObjectService guardedObjectService;
    @Value("${myconfig.host.node}")
    private String[] redisNodes;
    @Test
    public void test01() {
        System.out.println(guardedObjectService.get("yzq"));
    }

    @Test
    public void test02() {
        System.out.println(redisNodes);
    }
}
