package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
