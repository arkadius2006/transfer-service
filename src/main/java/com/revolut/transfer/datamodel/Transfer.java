package com.revolut.transfer.datamodel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Transfer {
    final private long fromId;
    final private long toId;
    final private double amount;

    public Transfer(long fromId, long toId, double amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "{ fromId : " + fromId + ", toId : " + toId + " , amount : " + amount + " }";
    }

    public static Transfer read(InputStream in) throws IOException {
        Properties properties = new Properties();
        properties.load(in);
        return new Transfer(
                Long.parseLong(properties.getProperty("transfer.fromId")),
                Long.parseLong(properties.getProperty("transfer.toId")),
                Double.parseDouble(properties.getProperty("transfer.amount")));
    }

    public static void write(Transfer transfer, OutputStream out) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("transfer.fromId", transfer.fromId + "");
        properties.setProperty("transfer.toId", transfer.getToId() + "");
        properties.setProperty("transfer.amount", transfer.getAmount() + "");

        properties.store(out, "");
    }
}
