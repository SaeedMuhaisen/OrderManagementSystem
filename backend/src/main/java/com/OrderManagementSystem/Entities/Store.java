package com.OrderManagementSystem.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Product> products=new ArrayList<>();
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StoreEmployee> employees=new ArrayList<>();

    @OneToMany(mappedBy = "store",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<OrderStore> orderStores=new HashSet<>();

}
