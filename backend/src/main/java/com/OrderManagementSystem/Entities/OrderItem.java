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
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Product product;
    private Integer quantity;
    @ManyToOne
    private Order order;
    @ManyToOne
    @JoinColumn(name="store_id")
    private Store store;
    private StatusType statusType;
    private Double productPrice;
}
