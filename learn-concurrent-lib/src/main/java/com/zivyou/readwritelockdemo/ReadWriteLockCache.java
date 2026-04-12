package com.zivyou.readwritelockdemo;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockCache {
    private final HashMap<String, String> cache = new HashMap<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public String get(String key) {
        String result = null;
        readLock.lock();
        try {
            result = cache.get(key);
        } finally {
            readLock.unlock();
        }
        if (result == null) {
            writeLock.lock();
            try {
                result = "xxx"; //从其他地方加载
                cache.put(key, result);
            } finally {
                writeLock.unlock();
            }
        }
        return result;
    }

    public boolean put(String key, String value) {
        writeLock.lock();
        try {
            return cache.put(key, value) != null;
        } finally {
            writeLock.unlock();
        }
    }
}
