package com.zivyou.completionservicedemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CompletionServiceDemo {

    private int randomTime() {
        return (int) (Math.random() * 10);
    }

    public void demo1() {
        Executor executor = Executors.newFixedThreadPool(3);
        CompletionService<Integer> service = new ExecutorCompletionService<>(executor);
        List<Future<Integer>> list = new ArrayList<>(3);

        Future<Integer> f1 = service.submit(() -> {
            Thread.sleep(randomTime()* 1000L);
            return 1;
        });

        Future<Integer> f2 = service.submit(() -> {
            Thread.sleep(randomTime()* 1000L);
            return 2;
        });

        Future<Integer> f3 = service.submit(() -> {
            Thread.sleep(randomTime()* 1000L);
            return 3;
        });

        list.add(f1); list.add(f2); list.add(f3);
        try {
            for (int i = 0; i < 3; i++) {
                if (service.take().get() != null) {
                    System.out.println("get the result: "+service.take().get());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            for (Future f : list) f.cancel(true);
        }
    }
}
