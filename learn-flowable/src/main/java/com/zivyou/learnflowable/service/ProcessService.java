package com.zivyou.learnflowable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessService {
    private final ProcessEngine processEngine;


}
