package com.zivyou.learnflowable.controller;

import com.zivyou.learnflowable.service.EventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {
    private final ProcessEngine processEngine;
    private final EventListener eventListener;
    private final ManagementService managementService;

    @GetMapping("/start")
    void startProcess() {
        var deployment = processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("processes/test1.bpmn20.xml")
                .name("test1")
                .deploy();
        processEngine.getRuntimeService().addEventListener(eventListener);
        var instance = processEngine.getRuntimeService()
                .startProcessInstanceByKey("test1");

        var historyService = processEngine.getHistoryService();
        var query = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instance.getId());
        var taskService = processEngine.getTaskService();
    }
}
