package com.revolut.transfer.service;

import com.revolut.transfer.datamodel.Account;
import com.revolut.transfer.datastore.Datastore;
import com.revolut.transfer.datastore.DatastoreException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Handles requests like: PUT /transferservice/accounts
 */
public class InsertAccountService implements Service {
    final private Datastore datastore;

    public InsertAccountService(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public boolean canServe(HttpExchange httpExchange) {
        return checkVerb(httpExchange.getRequestMethod())
                && checkPath(httpExchange.getRequestURI().getPath());
    }

    @Override
    public void serve(HttpExchange httpExchange) throws IOException {
        Account account = Account.read(httpExchange.getRequestBody());

        try {
            datastore.insertAccount(account);

            // send 200 to server
            httpExchange.sendResponseHeaders(200, 0);
            httpExchange.close();
        } catch (DatastoreException e) {
            // send 400 to server and error message
            httpExchange.sendResponseHeaders(400, 0);
            IOUtilities.writeMessage("Failed to insert account: " + e.getMessage(), httpExchange.getResponseBody());
            httpExchange.close();
        }
    }


    private boolean checkVerb(String verb) {
        return Objects.equals(verb, "PUT");
    }

    private boolean checkPath(String path) {
        return Objects.equals("/transferservice/accounts", path);
    }

}
