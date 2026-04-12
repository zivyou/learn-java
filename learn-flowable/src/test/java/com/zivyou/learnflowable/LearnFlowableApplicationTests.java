package com.zivyou.learnflowable;

import com.zivyou.learnflowable.service.EventListener;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.TaskService;
import org.flowable.job.api.Job;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@EnableAutoConfiguration
@Slf4j
class LearnFlowableApplicationTests {
    @Autowired
    public ProcessEngine processEngine;
    @Autowired
    private EventListener eventListener;
    @Autowired
    private ManagementService managementService;

    @Test
    void contextLoads() {
    }

    @Test
    void test01() throws InterruptedException {
        var deployment = processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("processes/test1.bpmn20.xml")
                .name("test1")
                .deploy();
        processEngine.getRuntimeService().addEventListener(eventListener);
        var instance = processEngine.getRuntimeService()
                .startProcessInstanceByKey("test1");
        var taskService = processEngine.getTaskService();
        processEngine.getRuntimeService().createProcessInstanceQuery().active().list();

        while (instance != null) {
            var currentTask = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
//            log.info("-----------------------> currentTask: {}, instanceId: {}, deploymentId: {}", currentTask, instance.getId(), deployment.getId());
            List<Job> jobs = managementService.createTimerJobQuery().processInstanceId(instance.getId()).executable().list();
            if (jobs.size() > 0) {
                log.info("2---------------------------> timer job: {}", jobs);
                for (var job : jobs) {
                    TimeUnit.SECONDS.sleep(3);
                    managementService.moveTimerToExecutableJob(job.getId());
                    managementService.executeJob(job.getId());
                }
            }
        }
        Thread.sleep(Duration.ofSeconds(60).toMillis());
        processEngine.close();
    }

}
