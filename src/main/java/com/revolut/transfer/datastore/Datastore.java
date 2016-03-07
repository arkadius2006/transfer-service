package com.revolut.transfer.datastore;

import com.revolut.transfer.datamodel.Account;
import com.revolut.transfer.datamodel.Transfer;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory account dao.
 */
public class Datastore {
    final private static Logger logger  = Logger.getLogger(Datastore.class);

    final private Map<String,Account> map = new HashMap<String,Account>();

    public synchronized Account getAccount(String id) {
        Account account = map.get(id);
        logger.info("Retrieved account for id #" + id + ": " + account);
        return account;
    }

    public synchronized void insertAccount(Account account) throws DatastoreException {
        if (map.containsKey(account.getId())) {
            throw new DatastoreException("Account #" + account.getId() + " already exists");
        }

        map.put(account.getId(), account);
        logger.info("Inserted account: " + account);
    }

    public synchronized void transfer(Transfer transfer) throws DatastoreException {
        Account fromAccount = map.get(transfer.getFromId());
        if (fromAccount == null) {
            throw new DatastoreException("Account #" + transfer.getFromId() + " not found");
        }

        Account toAccount = map.get(transfer.getToId());
        if (toAccount == null) {
            throw new DatastoreException("Account #" + transfer.getToId() + " not found");
        }

        if (transfer.getAmount() <= 0) {
            throw new DatastoreException("Transfer amount is negative: " + transfer.getAmount());
        }

        // check fromAccount has sufficient money
        if (fromAccount.getAmount() < transfer.getAmount()) {
            throw new DatastoreException("Not enough money on account #" + fromAccount.getId());
        }

        // do transfer
        map.put(fromAccount.getId(), addAmount(fromAccount, -transfer.getAmount()));
        map.put(toAccount.getId(), addAmount(toAccount, transfer.getAmount()));

        logger.info("Transfered: " + transfer);
    }

    private static Account addAmount(Account origin, double money) {
        return new Account(origin.getId(), origin.getAmount() + money);
    }
}
