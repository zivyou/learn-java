package com.zivyou.learnflowable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.springframework.stereotype.Service;

@Service("task1")
@Slf4j
@RequiredArgsConstructor
public class Task1 implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!------------> ");
    }
}
