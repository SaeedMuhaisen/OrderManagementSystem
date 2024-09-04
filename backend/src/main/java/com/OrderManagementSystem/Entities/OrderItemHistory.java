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
        private UUID productId;
        private Integer quantity;
        @ManyToOne
        private OrderHistory orderHistory;
        @ManyToOne
        @JoinColumn(name="store_id")
        private Store store;
        private StatusType statusType;
        private Double productPrice;
}