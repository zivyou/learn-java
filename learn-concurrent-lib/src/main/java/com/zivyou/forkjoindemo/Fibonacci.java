package com.zivyou.forkjoindemo;

import java.util.concurrent.RecursiveTask;

public class Fibonacci extends RecursiveTask<Integer> {
    private final Integer n;
    public Fibonacci(Integer n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n<=1) return n;
        Fibonacci f1 = new Fibonacci(n-1);
        Fibonacci f2 = new Fibonacci(n-2);
        f1.fork();
        return f2.compute()+f1.join();
    }
}
