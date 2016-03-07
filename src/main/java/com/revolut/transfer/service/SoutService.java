package com.revolut.transfer.service;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Prints request to sout. For testing purpose.
 */
public class SoutService implements Service {

    @Override
    public boolean canServe(HttpExchange httpExchange) {
        return true;
    }

    @Override
    public void serve(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        System.out.println(httpExchange.getRequestMethod() + " " + httpExchange.getRequestURI());
        System.out.println();

        InputStream in = httpExchange.getRequestBody();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        do {
            String line = br.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        } while (true);
        in.close();
        httpExchange.close();
    }
}
