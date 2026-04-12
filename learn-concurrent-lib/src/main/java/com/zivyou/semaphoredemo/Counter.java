package com.zivyou.semaphoredemo;

import java.util.concurrent.Semaphore;

public class Counter {
    static private int count = 0;
    static private final Semaphore semaphore = new Semaphore(6);
    static public int increase() throws InterruptedException {
        semaphore.acquire();
        try {
            Counter.count++;
        } finally {
            semaphore.release();
        }
        return count;
    }

    static public int decrease() throws InterruptedException {
        semaphore.acquire();
        try {
            Counter.count--;
        } finally {
            semaphore.release();
        }
        return count;
    }

    static public int get() throws InterruptedException {
        semaphore.acquire();
        try {
            return count;
        } finally {
            semaphore.release();
        }
    }
}
