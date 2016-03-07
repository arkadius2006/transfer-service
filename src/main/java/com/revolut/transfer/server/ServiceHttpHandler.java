package com.revolut.transfer.server;

import com.revolut.transfer.service.FallbackService;
import com.revolut.transfer.service.Service;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Delegates handling https request to one of the services.
 */
public class ServiceHttpHandler implements HttpHandler {
    final private static Logger logger = Logger.getLogger(ServiceHttpHandler.class);

    final private List<Service> services;

    public ServiceHttpHandler(List<Service> services) {
        this.services = services;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.info("Received request " + httpExchange.getRequestMethod() + " " + httpExchange.getRequestURI());

        Service service = lookupService(httpExchange);
        service.serve(httpExchange);
    }

    private Service lookupService(HttpExchange httpExchange) {
        for (Service s : services) {
            if (s.canServe(httpExchange)) {
                return s;
            }
        }

        return new FallbackService();
    }
}
