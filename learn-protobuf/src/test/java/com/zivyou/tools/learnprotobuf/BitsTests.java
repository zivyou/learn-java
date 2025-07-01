package com.zivyou.tools.learnprotobuf;

import com.zivyou.tools.proto.demo.CapSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class BitsTests {

    @Test
    public void test01() {
        com.zivyou.tools.proto.demo.CapSet capSet = com.zivyou.tools.proto.demo.CapSet
                .newBuilder()
                .setFlags(4)
                .build();
        log.info("capSet FLAGS_LAND_TAKEOFF: {}",  capSet.getFlags() & CapSet.Flags.FLAGS_LAND_TAKEOFF_VALUE);
    }
}
