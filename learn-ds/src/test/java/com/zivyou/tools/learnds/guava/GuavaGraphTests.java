package com.zivyou.tools.learnds.guava;

import com.google.common.graph.NetworkBuilder;
import com.zivyou.tools.learnds.guavagraph.FlightPath;
import com.zivyou.tools.learnds.guavagraph.Runway;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class GuavaGraphTests {
    private final static Runway runway1 = new Runway("r1");
    private final static Runway runway2 = new Runway("r2");
    private final static Runway runway3 = new Runway("r3");
    private final static Runway runway4 = new Runway("r4");

    private final static FlightPath flightPath1 = new FlightPath("fp1");
    private final static FlightPath flightPath2 = new FlightPath("fp2");

    @Test
    public void test01() {
        var networkBuilder = NetworkBuilder.directed().<Runway, FlightPath>immutable();
        networkBuilder.addEdge(runway1, runway2, flightPath1);
        networkBuilder.addEdge(runway3, runway4, flightPath2);
        var network = networkBuilder.build();

        log.info(network.nodes().toString());
        log.info(network.edges().toString());
        log.info("connection: {}", network.edgeConnecting(runway1, runway3).isPresent());
    }
}
