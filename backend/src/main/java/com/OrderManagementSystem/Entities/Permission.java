package com.OrderManagementSystem.Entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    SELLER_READ("seller:read"),
    SELLER_UPDATE("seller:update"),
    SELLER_CREATE("seller:create"),
    SELLER_DELETE("seller:delete"),
    BUYER_READ("buyer:read"),
    BUYER_CREATE("buyer:create"),
    ;

    @Getter
    private final String permission;
}