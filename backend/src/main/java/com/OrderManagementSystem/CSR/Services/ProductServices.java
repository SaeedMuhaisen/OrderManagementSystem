package com.OrderManagementSystem.CSR.Services;


import com.OrderManagementSystem.CSR.Repositories.ProductRepository;
import com.OrderManagementSystem.CSR.Repositories.StoreEmployeeRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Entities.Product;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Entities.enums.EmployeeRole;
import com.OrderManagementSystem.Exceptions.AuthExceptions.UserNotFoundException;
import com.OrderManagementSystem.Exceptions.StoreExceptions.UnAuthorizedEmployeeException;
import com.OrderManagementSystem.Mappers.ProductMapper;
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

    private final ProductRepository productRepository;
    private final StoreEmployeeRepository storeEmployeeRepository;

    public void createStoreProduct(UserDetails userDetails, CreateProductDTO createProductDTO) {
        var storeEmployee = storeEmployeeRepository.findByUser(((User) userDetails));
        if(storeEmployee.isEmpty() || !storeEmployee.get().getEmployeeRole().equals(EmployeeRole.ADMIN)){
            throw new UnAuthorizedEmployeeException("Store Employee has no Authorization to do this operation");
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

    public List<ProductDTO> getVisibleProductsByStoreId(String sellersId) {
        var products=productRepository.findByStore_IdAndVisibleIsTrue(UUID.fromString(sellersId));
        return ProductMapper.INSTANCE.productListToProductDTOList(products);
    }

    public List<ProductDTO> getStoreProductsByEmployee(UserDetails userDetails) {
        var storeEmployee = storeEmployeeRepository.findByUser(((User) userDetails));
        if(storeEmployee.isEmpty() || !storeEmployee.get().getEmployeeRole().equals(EmployeeRole.ADMIN)){
            throw new UnAuthorizedEmployeeException("Store Employee has no Authorization to do this operation");
        }
        return ProductMapper.INSTANCE.productListToProductDTOList(storeEmployee.get().getStore().getProducts());
    }

    public void updateStoreProduct(UserDetails userDetails, CreateProductDTO newProduct) {
        var storeEmployee = storeEmployeeRepository.findByUser(((User) userDetails));
        if(storeEmployee.isEmpty() || !storeEmployee.get().getEmployeeRole().equals(EmployeeRole.ADMIN)){
            throw new UnAuthorizedEmployeeException("Store Employee has no Authorization to do this operation");
        }
        var productCheck=productRepository.findById(UUID.fromString(newProduct.getId()));

        if(productCheck.isEmpty() || !productCheck.get().getStore().getId().equals(storeEmployee.get().getStore().getId())){
            throw new UnAuthorizedEmployeeException("Store Employee has no Authorization to do this operation");
        }

        var product=productCheck.get();

        product.setVisible(newProduct.isVisible());
        product.setAvailableQuantity(newProduct.getAvailableQuantity());
        product.setDescription(newProduct.getDescription());
        product.setName(newProduct.getName());
        product.setPrice(newProduct.getPrice());

        productRepository.save(product);

    }
}
