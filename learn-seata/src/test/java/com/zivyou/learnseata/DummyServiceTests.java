package com.zivyou.learnseata;

import com.zivyou.learnseata.service.DummyService;
import io.seata.saga.engine.StateMachineEngine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DummyServiceTests {
    @Autowired
    DummyService dummyService;

    @Autowired
    StateMachineEngine stateMachineEngine;

    @Test
    public void test01() {
        dummyService.commit(stateMachineEngine);
    }

    @Test
    public void test02() {
        dummyService.compensate(stateMachineEngine);
    }
}
