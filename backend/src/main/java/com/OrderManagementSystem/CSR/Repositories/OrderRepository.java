package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.Order;
import com.OrderManagementSystem.Entities.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.store.id = :storeId")
    List<Order> findAllByStoreId(UUID storeId);
}
