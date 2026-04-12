package com.zivyou.learnpaxos.db;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataMap {
    private final Map<String, String> dataMap = new ConcurrentHashMap<>();

    public void put(String key, String value) {
        dataMap.put(key, value);
    }

    public String get(String key) {
        return dataMap.get(key);
    }
}
