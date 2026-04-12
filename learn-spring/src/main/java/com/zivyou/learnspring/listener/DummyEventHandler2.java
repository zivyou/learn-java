package com.zivyou.learnspring.listener;

import com.zivyou.learnspring.event.DummyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DummyEventHandler2 {
    @EventListener(classes = DummyEvent.class)
    public void handle(DummyEvent event) {
        log.info("handle event from DummyEventHandler2, event: {}, context: {}", event, event.getSource());
    }
}
