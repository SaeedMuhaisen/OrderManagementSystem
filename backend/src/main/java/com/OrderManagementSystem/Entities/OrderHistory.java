package com.OrderManagementSystem.Entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter

public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer_id")
    private User buyer;
    private Instant created_t;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_store_history",
            joinColumns = @JoinColumn(name = "order_history_id"),
            inverseJoinColumns = @JoinColumn(name = "store_id")
    )
    private Set<Store> stores;

    @OneToMany(mappedBy = "orderHistory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItemHistory> orderItemHistories=new ArrayList<>();

}
