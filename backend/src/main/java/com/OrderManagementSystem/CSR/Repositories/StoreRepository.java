package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.Store;
import com.OrderManagementSystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {
    Optional<Store> findByEmployees_User(User storeEmployee);
}
