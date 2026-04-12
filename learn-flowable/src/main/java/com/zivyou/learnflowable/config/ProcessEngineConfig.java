package com.zivyou.learnflowable.config;

import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class ProcessEngineConfig {
    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource,
                                                                             PlatformTransactionManager transactionManager,
                                                                             ApplicationContext applicationContext) {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate("true");
        configuration.setTransactionManager(transactionManager);
        configuration.setApplicationContext(applicationContext);
//        configuration.setHistory("full");
//        configuration.setHistoryLevel(HistoryLevel.FULL);
//        configuration.setAsyncExecutorActivate(true);

//        ProcessExpressionManager springExpressionManager = new ProcessExpressionManager();
//        configuration.setExpressionManager(springExpressionManager);
        return configuration;
    }

    @Bean
    public ProcessEngine processEngine(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
        return springProcessEngineConfiguration.buildProcessEngine();
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }
}
