package com.zivyou.stampedlockdemo;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock: 基于版本的乐观读方案;
 * 悲观读锁 - 写锁 这一套与ReadWriteLock一模一样;
 * 但是乐观读的方案是无所方案，不与悲观锁/写锁互斥;
 */

public class Point {
    private int x;
    private int y;
    private final StampedLock stampedLock = new StampedLock();

    public double distance() {
        long stamp = stampedLock.tryOptimisticRead();
        int curX = x; int curY = y;
        if (stampedLock.validate(stamp)) {
            return Math.sqrt(curX*curX + curY*curY);
        } else {
            stamp = stampedLock.readLock();
            try {
                curX = x; curY = y;
                return Math.sqrt(curX*curX+curX*curY);
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
    }
}
