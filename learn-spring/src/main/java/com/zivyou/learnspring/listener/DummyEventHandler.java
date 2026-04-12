package com.zivyou.learnspring.listener;

import com.zivyou.learnspring.event.DummyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DummyEventHandler implements ApplicationListener<DummyEvent> {
    @Override
    public void onApplicationEvent(DummyEvent event) {
        log.info("receive dummy-event: {}, context: {}", event, event.getSource());
    }
}
