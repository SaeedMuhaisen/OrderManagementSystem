package com.OrderManagementSystem.Exceptions.OrderExceptions;

public class OrderStatusIllegalTransitionException extends RuntimeException{
    public OrderStatusIllegalTransitionException(String message) {
        super(message);
    }
}
