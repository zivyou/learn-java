package com.zivyou.learnflowable.service;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.event.FlowableSignalEvent;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventListener implements FlowableEventListener {
    @Override
    public void onEvent(FlowableEvent event) {
        log.info("!!!!!===========================> onEvent: {}", event);
        if (event.getType().name().equals("TIMER_SCHEDULED")) {
            log.info("????????????????????????????????????????");
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}
