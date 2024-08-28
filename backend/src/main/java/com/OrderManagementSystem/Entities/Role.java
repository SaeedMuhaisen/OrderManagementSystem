package com.OrderManagementSystem.Entities;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {
    BUYER, SELLER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}