package com.zivyou.learnspring.event;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
@ToString
public class DummyEvent extends ApplicationEvent {
    Long eventId;
    String message;
    Long type;

    public DummyEvent(Object source) {
        super(source);
    }

    public DummyEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
