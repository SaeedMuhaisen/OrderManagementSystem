package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.Store;
import com.OrderManagementSystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {
    Optional<Store> findByEmployees_User(User storeEmployee);
}
