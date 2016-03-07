package com.revolut.transfer.service;

import com.revolut.transfer.datamodel.Account;
import com.revolut.transfer.datamodel.Transfer;
import com.revolut.transfer.datastore.Datastore;
import com.revolut.transfer.datastore.DatastoreException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Objects;

/**
 * PUT /transferservice/transfers.
 */
public class TransferService implements Service {
    final private Datastore datastore;

    public TransferService(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public boolean canServe(HttpExchange httpExchange) {
        return checkVerb(httpExchange.getRequestMethod())
                && checkPath(httpExchange.getRequestURI().getPath());
    }

    @Override
    public void serve(HttpExchange httpExchange) throws IOException {
        Transfer transfer = Transfer.read(httpExchange.getRequestBody());

        try {
            datastore.transfer(transfer);

            // send 200 to server
            httpExchange.sendResponseHeaders(200, 0);
            httpExchange.close();
        } catch (DatastoreException e) {
            // send 400 to server and error message
            httpExchange.sendResponseHeaders(400, 0);
            IOUtilities.writeMessage("Failed to transfer money: " + e.getMessage(), httpExchange.getResponseBody());
            httpExchange.close();
        }
    }

    private boolean checkVerb(String verb) {
        return Objects.equals(verb, "PUT");
    }


    private boolean checkPath(String path) {
        return Objects.equals("/transferservice/transfers", path);
    }
}
