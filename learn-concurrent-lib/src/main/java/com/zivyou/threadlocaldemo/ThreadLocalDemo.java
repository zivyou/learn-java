package com.zivyou.threadlocaldemo;

import java.util.concurrent.atomic.AtomicLong;

public class ThreadLocalDemo {
    static final AtomicLong nextId = new AtomicLong(1);
    static final ThreadLocal<Long> threadLocal = ThreadLocal.withInitial(nextId::getAndIncrement);
    public static long getThreadId() {
        return threadLocal.get();
    }
}
