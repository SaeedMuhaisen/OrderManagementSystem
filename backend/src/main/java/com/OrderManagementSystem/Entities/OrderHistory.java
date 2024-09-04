package com.OrderManagementSystem.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class OrderHistory {
    @Id
    private UUID id;

    @ManyToOne(cascade =CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @OneToMany(mappedBy = "orderHistory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItemHistory> orderItemHistories =new ArrayList<>();
    private Instant orderDate;
}
