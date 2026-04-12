package com.zivyou.learnspring.listener;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.Duration;

public class MySpringRunListener implements SpringApplicationRunListener {

    public MySpringRunListener(SpringApplication application, String[] args) {}
    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        SpringApplicationRunListener.super.started(context, timeTaken);
        System.out.println("started !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        SpringApplicationRunListener.super.starting(bootstrapContext);

        System.out.println("starting !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        SpringApplicationRunListener.super.environmentPrepared(bootstrapContext, environment);
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        SpringApplicationRunListener.super.contextPrepared(context);
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        SpringApplicationRunListener.super.contextLoaded(context);
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        SpringApplicationRunListener.super.started(context);
    }

    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        SpringApplicationRunListener.super.ready(context, timeTaken);
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        SpringApplicationRunListener.super.running(context);
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        SpringApplicationRunListener.super.failed(context, exception);
    }
}
