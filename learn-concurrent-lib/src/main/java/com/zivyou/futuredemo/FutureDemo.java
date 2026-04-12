package com.zivyou.futuredemo;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class FutureDemo {

    private final ExecutorService executorService = new ThreadPoolExecutor(3, 5, 1,
            TimeUnit.SECONDS, new LinkedBlockingDeque<>());

    public void demo1() throws ExecutionException, InterruptedException {
        Future<?> result = executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("begin to run!");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("time over!");
            }
        });
        System.out.println("future finished: "+result.get());
    }

    public void demo2() throws ExecutionException, InterruptedException {
        Future<Integer> future = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("begin to call!");
                Thread.sleep(5000);
                System.out.println("time over!");
                return 1000;
            }
        });
        System.out.println("future finished: "+future.get());
    }

    public void demo3() throws ExecutionException, InterruptedException {
        Integer result = 0;
        Future<Integer> future = executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("begin to run!");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("time over!");
            }
        }, result);
        System.out.println("future finished: "+future.get());
        System.out.println("返回值判断: "+ (result == future.get()));
    }

    public void demo4() {

    }
}
