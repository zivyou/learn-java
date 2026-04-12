package com.zivyou.learnredis.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class RedisConfig {
    private final Executor executor = Executors.newFixedThreadPool(5);

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>>
            stringObjectRecordStreamMessageListenerContainer (
                    RedisConnectionFactory connectionFactory,
                    RedisTemplate<String, String> redisTemplate
    ) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                        .batchSize(10)
                        .executor(executor)
                        .errorHandler(Throwable::printStackTrace)
                        .pollTimeout(Duration.ZERO)
                        .serializer(new StringRedisSerializer())
                        .build();
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
                StreamMessageListenerContainer.create(connectionFactory, options);
        container.start();
        return container;
    }
}
