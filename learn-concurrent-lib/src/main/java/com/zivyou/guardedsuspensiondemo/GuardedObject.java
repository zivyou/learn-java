package com.zivyou.guardedsuspensiondemo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class GuardedObject<K, V> {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Map<K, V> gos = new ConcurrentHashMap<K, V>();

    public V get(K key) {
        lock.lock();
        try {
            while (!gos.containsKey(key)) {
                System.out.println("!!!=================> GuardedObject get()");
                condition.await(1, TimeUnit.MINUTES);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return gos.get(key);
    }

    public void onChanged(K key, V value) {
        lock.lock();
        try {
            gos.put(key, value);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

}
