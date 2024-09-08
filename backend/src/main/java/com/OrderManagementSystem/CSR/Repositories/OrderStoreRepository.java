package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.Order;
import com.OrderManagementSystem.Entities.OrderStore;
import com.OrderManagementSystem.Entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderStoreRepository extends JpaRepository<OrderStore, UUID> {
    List<OrderStore> findAllByStore(Store store);
    List<OrderStore> findAllByStoreAndFinishedIsFalse(Store store);
    Optional<OrderStore> findByOrderAndStore(Order order, Store store);

    List<OrderStore> findAllByOrder(Order order);
}
