package com.revolut.transfer.datamodel;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Account {
    final private long id;
    private double balance;

    public Account(long id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public synchronized long getId() {
        return id;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized void withdraw(double money) {
        if (money <= 0) {
            throw new IllegalArgumentException("money <= 0");
        }

        final double newBalance = balance - money;
        if (newBalance < 0.0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        balance = newBalance;
    }

    public synchronized void deposit(double money) {
        if (money <= 0) {
            throw new IllegalArgumentException("money <= 0");
        }

        balance = balance + money;
    }

    @Override
    public String toString() {
        return "{id : " + id + ", balance : " + balance + "}";
    }

    public static void write(Account account, OutputStream out) throws IOException {
        Properties properties = toProperties(account);
        properties.store(out, "");
    }

    private static Properties toProperties(Account account) {
        Properties properties = new Properties();
        properties.setProperty("account.id", account.getId() + "");
        properties.setProperty("account.balance", account.getBalance() + "");
        return properties;
    }

    public static Account read(InputStream in) throws IOException {
        Properties properties = new Properties();
        properties.load(in);
        return fromProperties(properties);
    }

    private static Account fromProperties(Properties properties) {
        return new Account(Long.parseLong(properties.getProperty("account.id")), Double.parseDouble(properties.getProperty("account.balance")));
    }
}

