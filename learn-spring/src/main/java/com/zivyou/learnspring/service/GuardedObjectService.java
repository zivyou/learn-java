package com.zivyou.learnspring.service;

import ch.qos.logback.core.spi.ContextAware;
import com.zivyou.guardedsuspensiondemo.GuardedObject;
import com.zivyou.learnspring.event.DummyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class GuardedObjectService {
    private final GuardedObject<String, String> guardedObject;
    private final ApplicationContext applicationContext;

    @PostConstruct
    void init() {
        CompletableFuture.runAsync(() -> {
            System.out.println("==================================+> before get guarded object");
            String value = guardedObject.get("yzq");
            System.out.println("-----------------------------------> get guarded object result: "+value);
        });
        applicationContext.publishEvent(new DummyEvent("hello?"));
    }

    public String get(String key) {
        return guardedObject.get(key);
    }
}
