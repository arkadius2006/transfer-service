package com.revolut.transfer.service;

import java.io.OutputStream;
import java.io.PrintWriter;

public class IOUtilities {

    private IOUtilities() {
        throw new AssertionError("Should not be instantiated");
    }

    public static void writeMessage(String message, OutputStream out) {
        PrintWriter pw = new PrintWriter(out);
        pw.print(message);
        pw.flush();
    }
}
