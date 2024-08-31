package com.OrderManagementSystem.CSR.Services;


import Mappers.ProductMapper;
import com.OrderManagementSystem.CSR.Repositories.ProductRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Entities.Product;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Models.DTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
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

    public List<ProductDTO> getAllProductsBySellersId(String sellersId) {
        var user= userRepository.getReferenceById(UUID.fromString(sellersId));
        return ProductMapper.INSTANCE.productListToProductDTOList(user.getProducts());
    }

    public List<ProductDTO> getAllProductsBySeller(UserDetails userDetails) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());
        return ProductMapper.INSTANCE.productListToProductDTOList(user.getProducts());
    }

    public List<StoreProductDTO> getAllAvailableProducts() {
        return ProductMapper.INSTANCE.productListToStoreProductDTOList(productRepository.findAllByVisibleIsTrueAndAvailableQuantityIsGreaterThanEqual(1));
    }


    public void getAllOrders(UserDetails userDetails) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());

    }

}
