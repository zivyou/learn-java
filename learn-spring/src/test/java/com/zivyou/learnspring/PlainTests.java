package com.zivyou.learnspring;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import com.sankuai.uav.uairport.base.DummyMessage;
import com.sankuai.uav.uairport.base.SomeContext;
import com.google.protobuf.Any;
import com.google.protobuf.GeneratedMessage;

@Slf4j
public class PlainTests {

    @Test
    void testProtobufAny() {
        var message = DummyMessage.newBuilder()
                .setContext(
                        Any.<SomeContext>pack(SomeContext.getDefaultInstance())

                );
    }
}
