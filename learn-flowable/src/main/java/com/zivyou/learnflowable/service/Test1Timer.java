package com.zivyou.learnflowable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("test1Timer")
@Slf4j
@RequiredArgsConstructor
public class Test1Timer implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        log.info("2 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
