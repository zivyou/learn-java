package com.zivyou.forkjoindemo;

public class ForkJoinDemo {
    public void demo1() {
        Fibonacci f = new Fibonacci(10);
        System.out.println(f.compute());
    }
}
