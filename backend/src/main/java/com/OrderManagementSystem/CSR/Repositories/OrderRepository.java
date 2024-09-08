package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.Order;
import com.OrderManagementSystem.Entities.OrderStore;
import com.OrderManagementSystem.Entities.Product;

import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Entities.enums.EmployeeRole;
import com.OrderManagementSystem.Exceptions.StoreExceptions.UnAuthorizedEmployeeException;
import com.OrderManagementSystem.Mappers.StoreMapper;
import com.OrderManagementSystem.Models.DTO.StoreOrderDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
