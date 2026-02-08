package org.example.tezkor.exception;

public class CourierNotAvailableException extends RuntimeException {
    public CourierNotAvailableException(String message) {
        super(message);
    }
}