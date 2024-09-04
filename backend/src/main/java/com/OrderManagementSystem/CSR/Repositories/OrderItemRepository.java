package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.Order;
import com.OrderManagementSystem.Entities.OrderItem;
import com.OrderManagementSystem.Entities.Product;
import com.OrderManagementSystem.Entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    List<OrderItem> findAllByOrderAndProduct_Store(Order order, Store store);
}
