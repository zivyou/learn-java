package com.zivyou.learnseata.config;

import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.engine.config.DbStateMachineConfig;
import io.seata.saga.engine.impl.ProcessCtrlStateMachineEngine;
import io.seata.saga.rm.StateMachineEngineHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class SeataConfig {

    @Bean("stateMachineEngine")
    public ProcessCtrlStateMachineEngine processCtrlStateMachineEngine(DbStateMachineConfig dbStateMachineConfig) {
        ProcessCtrlStateMachineEngine engine = new ProcessCtrlStateMachineEngine();
        engine.setStateMachineConfig(dbStateMachineConfig);
        return engine;
    }

    @Bean
    public DbStateMachineConfig dbStateMachineConfig(DataSource dataSource) {
        DbStateMachineConfig config = new DbStateMachineConfig();
        config.setDataSource(dataSource);
        Resource resource = new ClassPathResource("statelang/1.json");
        Resource[] resources = {resource};
        config.setResources(resources);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 3,
                TimeUnit.SECONDS, new PriorityBlockingQueue<>());
        config.setThreadPoolExecutor(threadPoolExecutor);
        config.setEnableAsync(true);
        config.setApplicationId("saga_example");
        config.setTxServiceGroup("my_test_tx_group");
        return config;
    }

    @Bean
    public StateMachineEngineHolder stateMachineEngineHolder(StateMachineEngine stateMachineEngine) {
        StateMachineEngineHolder holder = new StateMachineEngineHolder();
        holder.setStateMachineEngine(stateMachineEngine);
        return holder;
    }
}
