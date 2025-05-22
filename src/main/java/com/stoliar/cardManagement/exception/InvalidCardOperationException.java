package com.stoliar.cardManagement.exception;

public class InvalidCardOperationException extends RuntimeException {
    public InvalidCardOperationException(String message) {
        super(message);
    }
}