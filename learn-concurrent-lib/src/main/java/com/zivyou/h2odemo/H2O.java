package com.zivyou.h2odemo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class H2O {
    private final Lock lock = new ReentrantLock();
    private final Semaphore semaphore = new Semaphore(2);
    private final CyclicBarrier barrier = new CyclicBarrier(3);

    public H2O() {

    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        semaphore.acquire();
        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
        releaseHydrogen.run();
        try {
            barrier.await();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        lock.lock();
        // releaseOxygen.run() outputs "O". Do not change or remove this line.
        releaseOxygen.run();
        try {
            barrier.await();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}