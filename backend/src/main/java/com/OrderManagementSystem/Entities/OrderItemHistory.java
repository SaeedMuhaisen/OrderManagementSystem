package com.OrderManagementSystem.Entities;


import com.OrderManagementSystem.Entities.enums.StatusType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter

public class OrderItemHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne(cascade =CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "order_history_id")
    private OrderHistory orderHistory;

    private UUID productId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
    private Integer quantity;

    private StatusType statusType;
    private Double productPrice;

}