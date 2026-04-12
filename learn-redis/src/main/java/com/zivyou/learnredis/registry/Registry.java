package com.zivyou.learnredis.registry;

import com.zivyou.learnredis.service.RedisService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class Registry {
    private final RedisService redisService;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String MUTEX = "airport:registry:mutex";
    private static final String CLUSTER_KEY = "airport:com.zivyou.learn-java:cluster";
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private Map<String, String> clusters;
    private String ip;

    private void join() throws InterruptedException {
        if (!StringUtils.hasLength(ip)) {
            try {
                InetAddress address = InetAddress.getLocalHost();
                this.ip = address.getHostAddress();
            } catch (Exception e) {
                e.printStackTrace();
                this.ip = "0.0.0.0";
            }
        }

        while (!redisService.lock(MUTEX)) {
            log.error("try to acquire lock: {}", MUTEX);
            Thread.sleep(1000);
        }
        try {
            redisTemplate.<String,String>opsForHash().put(CLUSTER_KEY, ip, String.valueOf(System.currentTimeMillis()));
            redisTemplate.expire(CLUSTER_KEY, 60, TimeUnit.SECONDS);
            redisService.publish(CLUSTER_KEY, ip);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            while (!redisService.unlock(MUTEX)) ;
        }
    }

    private void onCluster() {

    }

    @PostConstruct
    void init() {
        redisService.subscribe(CLUSTER_KEY, (message, pattern) -> {
            if (!Arrays.toString(message.getBody()).equals(ip))
                clusters = redisTemplate.<String,String>opsForHash().entries(CLUSTER_KEY);
        });
        executorService.scheduleAtFixedRate(() -> {
            try {
                join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 10, TimeUnit.SECONDS);
    }
}
