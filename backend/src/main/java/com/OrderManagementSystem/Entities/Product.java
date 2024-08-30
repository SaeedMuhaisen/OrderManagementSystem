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
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User user;

    private String name;
    private String description;
    private Double price;
    private Instant created_t;
    private Integer availableQuantity;
    private Integer amountSold;
    private Integer amountReturned;
    private boolean visible;

}