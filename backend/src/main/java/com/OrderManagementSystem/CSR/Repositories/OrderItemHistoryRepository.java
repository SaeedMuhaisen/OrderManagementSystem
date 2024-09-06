package com.OrderManagementSystem.CSR.Repositories;



import com.OrderManagementSystem.Entities.OrderHistory;
import com.OrderManagementSystem.Entities.OrderItemHistory;
import com.OrderManagementSystem.Entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemHistoryRepository extends JpaRepository<OrderItemHistory, UUID> {

//    List<OrderItemHistory> findAllByOrderHistoryAndOrderHistory_Stores(OrderHistory orderHistory, Store store);
    List<OrderItemHistory> findAllByOrderHistoryAndStore(OrderHistory orderHistory,Store store);
}
