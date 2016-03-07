package com.revolut.transfer.service;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Fallback service.
 */
public class FallbackService implements Service {

    @Override
    public boolean canServe(HttpExchange httpExchange) {
        return true;
    }

    @Override
    public void serve(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(400, 0);
        OutputStream out = httpExchange.getResponseBody();
        IOUtilities.writeMessage("Failed to serve request", out);
        out.close();
    }
}
