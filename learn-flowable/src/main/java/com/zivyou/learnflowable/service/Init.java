package com.zivyou.learnflowable.service;

import lombok.extern.slf4j.Slf4j;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.springframework.stereotype.Service;

@Service("init")
@Slf4j
public class Init implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("3. !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
