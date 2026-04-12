package com.zivyou.learnreactor.plain;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class ReactorTests {

    @Test
    public void test01() {
        var mono = Mono.<String>create(sink -> {
            
        });
        mono.block();
    }
}
