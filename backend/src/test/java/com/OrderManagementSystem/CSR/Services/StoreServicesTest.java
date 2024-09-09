package com.OrderManagementSystem.CSR.Services;

import com.OrderManagementSystem.CSR.Repositories.CustomerOrderTestHelper;
import com.OrderManagementSystem.CSR.Repositories.ProductRepository;
import com.OrderManagementSystem.CSR.Repositories.StoreRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class StoreServicesTest {


    @Autowired
    private CustomerOrderTestHelper utils;

    @Autowired
    private StoreServices storeServices;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;
    public final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        utils.deleteAll();
    }

    @AfterEach
    void tearDown() {
        utils.deleteAll();
    }


    @Test
    /**
    * Store should be visible only if it has atleast a single product that is visible with Available quantity more than 0
    * */
    void getAllStoresWithVisibleProducts() {
        var store=utils.createStore();
        var user=utils.createSeller();
        var storeEmployee=utils.createStoreEmployeeWithUserAndStore(user,store);
        var availableStores=storeServices.getAllAvailableStores();

        assert(availableStores.isEmpty());
        var product=utils.createProductForStore(store);
        availableStores=storeServices.getAllAvailableStores();

        assert(!availableStores.isEmpty());
        assert(availableStores.get(0).getSellerId().equals(String.valueOf(store.getId())));

        var storeCheck=storeRepository.findById(store.getId());
        if(storeCheck.isEmpty())
            fail();

        store=storeCheck.get();

        assert(!store.getEmployees().isEmpty());
        assert (store.getEmployees().get(0).getId().equals(storeEmployee.getId()));
        assert(store.getEmployees().get(0).getUser().getId().equals(user.getId()));
        assert(store.getEmployees().get(0).getStore().getId().equals(store.getId()));

        var productCheck=productRepository.findById(product.getId());
        if(productCheck.isEmpty())
            fail();

        product=productCheck.get();
        product.setVisible(false);
        productRepository.save(product);

        availableStores=storeServices.getAllAvailableStores();

        assert(availableStores.isEmpty());

        product.setVisible(true);
        product.setAvailableQuantity(0);
        productRepository.save(product);

        availableStores=storeServices.getAllAvailableStores();
        assert(availableStores.isEmpty());

        product.setAvailableQuantity(1);
        productRepository.save(product);

        availableStores=storeServices.getAllAvailableStores();
        assert(availableStores.size()==1);

    }
}