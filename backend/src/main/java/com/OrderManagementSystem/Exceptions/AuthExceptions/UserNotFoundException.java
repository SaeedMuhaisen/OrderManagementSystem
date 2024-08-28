package com.OrderManagementSystem.Exceptions.AuthExceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

