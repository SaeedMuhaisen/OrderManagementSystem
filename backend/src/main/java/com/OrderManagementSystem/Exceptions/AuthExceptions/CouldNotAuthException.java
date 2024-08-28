package com.OrderManagementSystem.Exceptions.AuthExceptions;

public class CouldNotAuthException extends RuntimeException {
    public CouldNotAuthException(String message) {
        super(message);
    }
}
