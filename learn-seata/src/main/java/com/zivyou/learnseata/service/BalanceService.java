package com.zivyou.learnseata.service;

import java.math.BigDecimal;
import java.util.Map;

public interface BalanceService {
    /**
     * reduce
     *
     * @param businessKey
     * @param amount
     * @param params
     * @return
     */
    boolean reduce(String businessKey, BigDecimal amount, Map<String, Object> params);

    /**
     * compensateReduce
     *
     * @param businessKey
     * @param params
     * @return
     */
    boolean compensateReduce(String businessKey, Map<String, Object> params);
}
