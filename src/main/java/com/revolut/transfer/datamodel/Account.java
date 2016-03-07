package com.revolut.transfer.datamodel;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Account {
    final private String id;
    final private double amount;

    public Account(String id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "{id : " + id + ", amount : " + amount + "}";
    }

    public static void write(Account account, OutputStream out) throws IOException {
        Properties properties = toProperties(account);
        properties.store(out, "");
    }

    private static Properties toProperties(Account account) {
        Properties properties = new Properties();
        properties.setProperty("account.id", account.getId());
        properties.setProperty("account.amount", account.getAmount() + "");
        return properties;
    }

    public static Account read(InputStream in) throws IOException {
        Properties properties = new Properties();
        properties.load(in);
        return fromProperties(properties);
    }

    private static Account fromProperties(Properties properties) {
        return new Account(properties.getProperty("account.id"), Double.parseDouble(properties.getProperty("account.amount")));
    }
}

