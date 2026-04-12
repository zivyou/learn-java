package com.zivyou.learnseata.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Override
    public boolean reduce(String businessKey, int count) {
        LOGGER.info("reduce inventory succeed, count: " + count + ", businessKey:" + businessKey);
        return true;
    }

    @Override
    public boolean compensateReduce(String businessKey) {
        LOGGER.info("compensate reduce inventory succeed, businessKey:" + businessKey);
        return true;
    }
}
