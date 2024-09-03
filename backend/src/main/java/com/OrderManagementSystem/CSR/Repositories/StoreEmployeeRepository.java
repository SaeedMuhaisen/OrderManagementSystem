package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.StoreEmployee;
import com.OrderManagementSystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreEmployeeRepository extends JpaRepository<StoreEmployee, UUID> {
    Optional<StoreEmployee> findByUser(User user);
}
