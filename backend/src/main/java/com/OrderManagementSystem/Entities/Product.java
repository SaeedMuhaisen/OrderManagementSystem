package com.OrderManagementSystem.Entities;

import com.OrderManagementSystem.Entities.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    public User user;

    public String name;
    public String description;
    public Double price;
    public Instant created_t;
    public Integer availableQuantity;
    public Integer amountSold;
    public Integer amountReturned;
    public boolean visible;

}