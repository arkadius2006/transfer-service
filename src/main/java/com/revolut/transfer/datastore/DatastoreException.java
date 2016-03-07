package com.revolut.transfer.datastore;

/**
 * Indicates that data access operation failed
 */
public class DatastoreException extends Exception {

    public DatastoreException(String message) {
        super(message);
    }

    public DatastoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
