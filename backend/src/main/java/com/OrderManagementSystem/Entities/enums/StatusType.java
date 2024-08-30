package com.OrderManagementSystem.Entities.enums;


public enum StatusType {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    DISPATCHED("Dispatched"),
    DELIVERED("Delivered"),
    CANCELED_BY_BUYER("Canceled By Buyer"),
    CANCELED_BY_SELLER("Canceled By Seller");

    private final String displayName;

    StatusType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}