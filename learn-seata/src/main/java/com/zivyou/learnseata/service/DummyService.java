package com.zivyou.learnseata.service;

import io.seata.saga.engine.StateMachineEngine;

public interface DummyService {
    void commit(StateMachineEngine stateMachineEngine);
    void compensate(StateMachineEngine stateMachineEngine);
}
