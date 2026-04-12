package com.zivyou.service;

import com.zivyou.pojo.Demo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleService {
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);

    public void start() {
        executorService.scheduleAtFixedRate(() -> {
            System.out.println("============================> running...");
            Demo demo = Demo.builder().name("hello").id("world").value((int) (Math.random()*1000)).build();
            System.out.println(demo);
        }, 1, 1, TimeUnit.SECONDS);
    }
}
