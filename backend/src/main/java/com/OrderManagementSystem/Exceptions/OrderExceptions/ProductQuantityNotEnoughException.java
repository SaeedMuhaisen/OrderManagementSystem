package com.OrderManagementSystem.Exceptions.OrderExceptions;

public class ProductQuantityNotEnoughException extends RuntimeException {
    public ProductQuantityNotEnoughException(String message) {
        super(message);
    }
}
