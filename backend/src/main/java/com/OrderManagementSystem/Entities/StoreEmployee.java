package com.OrderManagementSystem.Entities;

import com.OrderManagementSystem.Entities.enums.EmployeeRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class StoreEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private EmployeeRole employeeRole;

}
