package com.revolut.transfer.service;

import com.revolut.transfer.datamodel.Account;
import com.revolut.transfer.datastore.Datastore;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Serves requests like: GET /transferservice/accounts/1
 */
public class GetAccountService implements Service {
    final private Datastore datastore;

    public GetAccountService(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public boolean canServe(HttpExchange httpExchange) {
        return checkVerb(httpExchange.getRequestMethod())
                && checkPath(httpExchange.getRequestURI().getPath());
    }

    @Override
    public void serve(HttpExchange httpExchange) throws IOException {
        String accountId = getAccountId(httpExchange.getRequestURI().getPath());

        Account account = datastore.getAccount(accountId);
        if (account != null) {
            httpExchange.sendResponseHeaders(200, 0);
            Account.write(account, httpExchange.getResponseBody());
        } else {
            httpExchange.sendResponseHeaders(400, 0);
            IOUtilities.writeMessage("Account not found", httpExchange.getResponseBody());
        }

        httpExchange.getResponseBody().close();
    }

    private boolean checkVerb(String verb) {
        return Objects.equals("GET", verb);
    }

    private boolean checkPath(String path) {
        if (path == null) {
            return false;

        }

        String[] parts = path.split("/");
        if (parts.length != 4) {
            return false;
        }

        if (!"".equals(parts[0])) {
            return false;
        }

        if (!"transferservice".equals(parts[1])) {
            return false;
        }

        if (!"accounts".equals(parts[2])) {
            return false;
        }

        if (parts[3] == null || parts[3].equals("")) {
            return false;
        }

        return true;

    }

    private String getAccountId(String path) {
        String[] parts = path.split("/");
        return parts[3];
    }
}
