package com.zivyou.countdownlatchdemo;

import java.util.concurrent.CountDownLatch;

public class Counter {
    private final CountDownLatch latch = new CountDownLatch(5);
    private static int count = 5;

    public int decrease() throws InterruptedException {
        latch.countDown();
        count--;
        latch.await();
        return count;
    }

    public int increase() throws InterruptedException {
        latch.countDown();
        count++;
        latch.await();
        return count;
    }
}
