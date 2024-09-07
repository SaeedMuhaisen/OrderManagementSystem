package com.OrderManagementSystem.CSR.Services;


import com.OrderManagementSystem.CSR.Repositories.ProductRepository;
import com.OrderManagementSystem.CSR.Repositories.StoreEmployeeRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Entities.Product;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Entities.enums.EmployeeRole;
import com.OrderManagementSystem.Exceptions.StoreExceptions.UnAuthorizedEmployeeException;
import com.OrderManagementSystem.Models.DTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServices {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final StoreEmployeeRepository storeEmployeeRepository;


    public void createStoreProduct(UserDetails userDetails, CreateProductDTO createProductDTO) {
        var user= userRepository.findById(((User) userDetails).getId());
        var storeEmployee = storeEmployeeRepository.findByUser(user.get());

        if(storeEmployee.isEmpty() || !storeEmployee.get().getEmployeeRole().equals(EmployeeRole.ADMIN)){
            throw new UnAuthorizedEmployeeException("User has no Authorization to do this operation");
        }

        var product= Product.builder()
                .id(UUID.fromString(createProductDTO.getId()))
                .name(createProductDTO.getName())
                .description(createProductDTO.getDescription())
                .price(createProductDTO.getPrice())
                .created_t(Instant.now())
                .amountSold(0)
                .amountReturned(0)
                .availableQuantity(createProductDTO.getAvailableQuantity())
                .visible(createProductDTO.isVisible())
                .store(storeEmployee.get().getStore())
                .build();
        productRepository.save(product);
    }





}
