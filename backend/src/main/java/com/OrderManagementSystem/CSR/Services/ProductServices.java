package com.OrderManagementSystem.CSR.Services;

import com.OrderManagementSystem.CSR.Repositories.ProductRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Entities.Product;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Models.DTO.CreateProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ProductServices {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public void createProduct(UserDetails userDetails,CreateProductDTO createProductDTO) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());

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
                .user(user)
                .build();

        productRepository.save(product);

    }
}
