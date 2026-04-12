package com.zivyou.learnseata.server;

import io.seata.server.Server;

import java.io.IOException;

public class SeataServer {

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.main(args);
    }
}
