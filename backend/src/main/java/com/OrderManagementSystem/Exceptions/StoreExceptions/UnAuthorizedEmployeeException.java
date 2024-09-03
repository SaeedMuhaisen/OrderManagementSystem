package com.OrderManagementSystem.Exceptions.StoreExceptions;

public class UnAuthorizedEmployeeException extends RuntimeException {
    public UnAuthorizedEmployeeException(String message) {
        super(message);
    }
}
