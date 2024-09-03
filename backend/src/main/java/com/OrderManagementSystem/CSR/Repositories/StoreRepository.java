package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {


}
