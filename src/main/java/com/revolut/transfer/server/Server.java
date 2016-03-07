package com.revolut.transfer.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Simple HTTP server.
 */
public class Server {
    final private int port;
    final private String contentRoot;
    final private HttpHandler handler;

    private HttpServer httpServer;

    public Server(int port, String contentRoot, HttpHandler handler) {
        this.port = port;
        this.contentRoot = contentRoot;
        this.handler = handler;
    }

    public void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext(contentRoot, handler);
        httpServer.setExecutor(null);

        httpServer.start();
    }

}
