package com.OrderManagementSystem.Entities.enums;


public enum StatusType {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    DISPATCHED("Dispatched"),
    DELIVERED("Delivered"),
    CANCELED_BY_BUYER("Canceled By Buyer"),
    CANCELED_BY_SELLER("Canceled By Seller"),
    CANCELED_BY_ADMIN_MANUALLY("Canceled By Admin"); //Manually Canceling an order by admin || cannot be inputted or updated to by user(buyer/seller);

    private final String displayName;

    StatusType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isIllegalStatusTransition(StatusType oldStatus, StatusType newStatus) {
        return switch (oldStatus) {
            case PENDING -> newStatus==PENDING;

            case ACCEPTED -> newStatus==PENDING || newStatus==ACCEPTED;

            case DISPATCHED -> newStatus==ACCEPTED || newStatus==PENDING || newStatus==CANCELED_BY_BUYER;

            default -> false;
        };
    }
}