package com.revolut.transfer.service;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;


/**
 * Abstract service that can handle HTTP requests.
 */
public interface Service {

    public boolean canServe(HttpExchange httpExchange);

    public void serve(HttpExchange httpExchange) throws IOException;
}
