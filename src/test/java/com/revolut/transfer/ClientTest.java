package com.revolut.transfer;

import com.revolut.transfer.datamodel.Account;
import com.revolut.transfer.datamodel.Transfer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ClientTest {

    private TransferMain server;
    private Client client;

    @Before
    public void setUp() throws Exception {
        server = new TransferMain();
        server.start();
        client = new Client();
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Test
    public void transferFifty() throws IOException {
        // insert
        client.insert(new Account(1L, 100.0));
        client.insert(new Account(2L, 200.0));

        // check
        Assert.assertEquals("Account 1", 100.0, client.get(1L).getBalance(), 1E-5);
        Assert.assertEquals("Account 1", 200.0, client.get(2L).getBalance(), 1E-5);

        // transfer
        client.transter(new Transfer(1, 2, 50.0));


        // check again
        Assert.assertEquals("Account 1", 50.0, client.get(1L).getBalance(), 1E-5);
        Assert.assertEquals("Account 1", 250.0, client.get(2L).getBalance(), 1E-5);

    }
}
