package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.Order;

import com.OrderManagementSystem.Entities.OrderHistory;
import com.OrderManagementSystem.Entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, UUID> {
//    @Query("SELECT DISTINCT oh FROM OrderHistory oh JOIN store oih WHERE oih.store.id = :storeId")
//    List<OrderHistory> findAllByStoreId(UUID storeId);

    List<OrderHistory> findAllByStores(Store store);
}
