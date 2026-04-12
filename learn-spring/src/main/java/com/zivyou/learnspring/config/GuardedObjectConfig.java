package com.zivyou.learnspring.config;

import com.zivyou.guardedsuspensiondemo.GuardedObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GuardedObjectConfig {

    @Bean
    GuardedObject<String, String> guardedObject() {
        return new GuardedObject<>();
    }
}
