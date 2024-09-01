package com.OrderManagementSystem.Exceptions.OrderExceptions;

public class OrderContainsIllegalProduct extends RuntimeException {
    public OrderContainsIllegalProduct(String message) {
        super(message);
    }
}
