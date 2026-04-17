package com.zivyou.h2odemo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class H2OCountDownLatch {
    private final Semaphore oxygenSemaphore = new Semaphore(1);
    private final Semaphore hydrogenSemaphore = new Semaphore(2);
    private CountDownLatch latch = new CountDownLatch(3);

    public H2OCountDownLatch() {
    }

    private synchronized void resetLatch() {
        if (latch.getCount() == 0) {
            latch = new CountDownLatch(3);
            oxygenSemaphore.release();
            hydrogenSemaphore.release(2);
        }
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hydrogenSemaphore.acquire();
        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
        releaseHydrogen.run();
        latch.countDown();
        latch.await();
        resetLatch();
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oxygenSemaphore.acquire();
        // releaseOxygen.run() outputs "O". Do not change or remove this line.
        releaseOxygen.run();
        latch.countDown();
        latch.await();
        resetLatch();
    }
}
