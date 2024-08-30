package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByVisibleIsTrueAndAvailableQuantityIsGreaterThanEqual(Integer amount);
}
