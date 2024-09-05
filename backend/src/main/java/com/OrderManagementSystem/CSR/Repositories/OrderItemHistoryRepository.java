package com.OrderManagementSystem.CSR.Repositories;



import com.OrderManagementSystem.Entities.OrderItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemHistoryRepository extends JpaRepository<OrderItemHistory, UUID> {
}
