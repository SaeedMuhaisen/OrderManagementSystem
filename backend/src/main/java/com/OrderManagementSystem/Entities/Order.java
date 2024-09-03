package com.OrderManagementSystem.Entities;

import com.OrderManagementSystem.Entities.enums.StatusType;
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
@Table(name = "_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(cascade =CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems=new ArrayList<>();
    private Instant created_t;

}
