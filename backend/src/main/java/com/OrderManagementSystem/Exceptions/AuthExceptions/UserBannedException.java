package com.OrderManagementSystem.Exceptions.AuthExceptions;

public class UserBannedException extends RuntimeException {
    public UserBannedException(String message) {
        super(message);
    }
}
