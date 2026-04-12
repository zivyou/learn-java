package com.zivyou.learnspring;

import com.zivyou.learnspring.listener.DummyEventHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LearnSpringApplication {

    public static void main(String[] args) {
        var application = new SpringApplication(LearnSpringApplication.class);
        application.run(args);
    }

}
