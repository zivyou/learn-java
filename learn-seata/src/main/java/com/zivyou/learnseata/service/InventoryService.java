package com.zivyou.learnseata.service;

import java.math.BigDecimal;
import java.util.Map;

public interface InventoryService {
    boolean reduce(String businessKey, int count);
    boolean compensateReduce(String businessKey);
}
