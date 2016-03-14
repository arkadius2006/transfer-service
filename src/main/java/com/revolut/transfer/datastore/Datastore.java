package com.revolut.transfer.datastore;

import com.revolut.transfer.datamodel.Account;
import com.revolut.transfer.datamodel.Transfer;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory account dao.
 */
public class Datastore {
    final private static Logger logger  = Logger.getLogger(Datastore.class);

    final private ConcurrentMap<Long,Account> map = new ConcurrentHashMap<Long, Account>();

    public Account getAccount(long id) throws DatastoreException {
        Account account = map.get(id);
        if (account == null) {
            throw new DatastoreException("No such account: " + id);
        }
        logger.info("Retrieved account for id #" + id + ": " + account);
        return account;
    }

    public void insertAccount(Account account) throws DatastoreException {
        Account prevAccount = map.putIfAbsent(account.getId(), account);
        if (prevAccount != null) {
            throw new DatastoreException("Account #" + account.getId() + " already exists");
        }
        logger.info("Inserted account: " + account);
    }

    public void transfer(Transfer transfer) throws DatastoreException {
        transfer(transfer.getFromId(), transfer.getToId(), transfer.getAmount());
    }

    private void transfer(long fromId, long toId, double money) throws DatastoreException {
        transfer(getAccount(fromId), getAccount(toId), money);
    }

    private void transfer(Account from, Account to, double money) throws DatastoreException {
        if (money <= 0) {
            throw new IllegalArgumentException("money <=0");
        }

        Account first;
        Account second;

        if (from.getId() <= to.getId()) {
            first = from;
            second = to;
        } else {
            first = to;
            second = from;
        }

        // acquire monitors in predefined order to avoid deadlock
        synchronized (first) {
            synchronized (second) {
                from.withdraw(money); // this method will throw Exception if not sufficient funds
                to.deposit(money);
            }
        }
    }
}
