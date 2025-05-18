package com.zivyou.learn.learnjuc.future;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

@Slf4j
public class FutureTests {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private Future<String> future01() {
        return executorService.submit(() -> {
            TimeUnit.SECONDS.sleep(5);
            return "01";
        });
    }

    private Future<String> future02() {
        return executorService.submit(() -> {
            TimeUnit.SECONDS.sleep(3);
            return "02";
        });
    }

    @Test
    public void test01() throws ExecutionException, InterruptedException {
        var start = System.currentTimeMillis();
        var f1 = future01();
        var f2 = future02();
        f1.get();
        f2.get();
        var end = System.currentTimeMillis();
        log.info("time duration:{}", (end - start) / 1000);

    }
}
