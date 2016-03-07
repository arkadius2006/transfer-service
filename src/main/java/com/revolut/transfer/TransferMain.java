package com.revolut.transfer;

import com.revolut.transfer.datastore.Datastore;
import com.revolut.transfer.datastore.DatastoreException;
import com.revolut.transfer.server.Server;
import com.revolut.transfer.service.*;
import com.revolut.transfer.server.ServiceHttpHandler;
import com.sun.net.httpserver.HttpHandler;

import java.util.ArrayList;
import java.util.List;

public class TransferMain {
    final private static String CONTENT_ROOT = "/transferservice";
    final private static int PORT = 8080;

    public static void main(String args[]) throws Exception {
        // datastore
        Datastore datastore = createDatastore();

        // services and http handler
        List<Service> services = createServices(datastore);
        HttpHandler handler = new ServiceHttpHandler(services);

        // http server
        Server server = new Server(PORT, CONTENT_ROOT, handler);

        server.start();
    }

    private static Datastore createDatastore() throws DatastoreException {
        Datastore datastore = new Datastore();
        // datastore.insertAccount(new Account("1", 100));
        // datastore.insertAccount(new Account("2", 200));
        // datastore.insertAccount(new Account("3", 300));

        return datastore;
    }

    private static List<Service> createServices(Datastore datastore) {
        List<Service> services = new ArrayList<Service>();
        // services.add(new SoutService());
        services.add(new GetAccountService(datastore));
        services.add(new InsertAccountService(datastore));
        services.add(new TransferService(datastore));
        return services;
    }

}
