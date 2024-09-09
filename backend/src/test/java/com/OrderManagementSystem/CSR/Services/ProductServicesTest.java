package com.OrderManagementSystem.CSR.Services;


import com.OrderManagementSystem.CSR.Repositories.CustomerOrderTestHelper;
import com.OrderManagementSystem.CSR.Repositories.ProductRepository;
import com.OrderManagementSystem.CSR.Repositories.StoreRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;

import com.OrderManagementSystem.Entities.Store;
import com.OrderManagementSystem.Exceptions.StoreExceptions.UnAuthorizedEmployeeException;
import com.OrderManagementSystem.Models.DTO.CreateProductDTO;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.UUIDClock;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServicesTest {

    @Autowired
    private CustomerOrderTestHelper utils;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductServices productServices;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        utils.deleteAll();
    }

    @AfterEach
    void tearDown() {
        utils.deleteAll();
    }

    @Test
    @Transactional
    void createStoreProduct(){
        var createProductDTO=
                CreateProductDTO
                        .builder()
                        .id(String.valueOf(UUID.randomUUID()))
                        .name("Product 1")
                        .description("Test description")
                        .price(20.0)
                        .availableQuantity(10)
                        .visible(true)
                        .build();
        var customerUser=utils.createCustomer();
        var sellerUser = utils.createSeller();
        var store=utils.createStore();
        var storeEmployee=utils.createStoreEmployeeWithUserAndStore(sellerUser,store);

        assertThrows(UnAuthorizedEmployeeException.class,
                () -> productServices.createStoreProduct(customerUser, createProductDTO));
        productServices.createStoreProduct(sellerUser,createProductDTO);

        var productCheck=productRepository.findById(UUID.fromString(createProductDTO.getId()));
        assert(productCheck.isPresent());
        var product=productCheck.get();

        assert(product.getId().equals(UUID.fromString(createProductDTO.getId())));
        assert(product.getName().equals(createProductDTO.getName()));
        assert(product.getDescription().equals(createProductDTO.getDescription()));
        assert(product.getPrice().equals(createProductDTO.getPrice()));
        assert(product.getAvailableQuantity().equals(createProductDTO.getAvailableQuantity()));
        assert(product.getAmountSold().equals(0));
        assert(product.getAmountReturned().equals(0));
        assert(product.isVisible());
        assert(product.getStore().getId().equals(store.getId()));


    }
    @Test
    void getStoreProductsByEmployee(){
        var customerUser=utils.createCustomer();
        var sellerUser1 = utils.createSeller();
        var sellerUser2 = utils.createSeller();
        var store1=utils.createStore();
        var store2=utils.createStore();
        var storeEmployee1=utils.createStoreEmployeeWithUserAndStore(sellerUser1,store1);
        var storeEmployee2=utils.createStoreEmployeeWithUserAndStore(sellerUser2,store2);

        var productStore1=utils.createProductForStore(store1);
        var productStore2=utils.createProductForStore(store2);

        assertThrows(UnAuthorizedEmployeeException.class,
                () -> productServices.getStoreProductsByEmployee(customerUser));
        var store1Products=productServices.getStoreProductsByEmployee(sellerUser1);
        var store2Products=productServices.getStoreProductsByEmployee(sellerUser2);

        assert(store1Products.size()==1);
        assert(store2Products.size()==1);

        assert(store1Products.get(0).getId().equals(String.valueOf(productStore1.getId())));

        assert(store2Products.get(0).getId().equals(String.valueOf(productStore2.getId())));

    }
    @Test
    void getVisibleProductsByStoreId(){
        var customerUser=utils.createCustomer();
        var sellerUser1 = utils.createSeller();
        var store1=utils.createStore();
        var product= utils.createProductForStore(store1);

        var products=productServices.getVisibleProductsByStoreId(String.valueOf(store1.getId()));

        assert (products.size()==1);
        assert (products.get(0).isVisible());

        var updatedProduct=productRepository.findById(product.getId());
        assert (updatedProduct.isPresent());
        updatedProduct.get().setVisible(false);
        productRepository.save(updatedProduct.get());

        products=productServices.getVisibleProductsByStoreId(String.valueOf(store1.getId()));
        assert (products.isEmpty());

    }
}