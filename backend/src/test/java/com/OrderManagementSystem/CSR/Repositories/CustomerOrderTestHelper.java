package com.OrderManagementSystem.CSR.Repositories;



import com.OrderManagementSystem.Entities.Product;
import com.OrderManagementSystem.Entities.Store;
import com.OrderManagementSystem.Entities.StoreEmployee;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Entities.enums.EmployeeRole;
import com.OrderManagementSystem.Entities.enums.Role;
import com.OrderManagementSystem.Entities.enums.UserStatus;
import com.github.javafaker.Faker;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;


@Component
public class CustomerOrderTestHelper {

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    StoreEmployeeRepository storeEmployeeRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    public final Faker faker=new Faker();

    @Transactional
    public User createCustomer(){
        var customer=  User.builder()
                        .email(faker.bothify("????##@gmail.com"))
                        .userStatus(UserStatus.ACTIVE)
                        .role(Role.BUYER)
                        .firstname(faker.name().firstName())
                        .lastname(faker.name().lastName())
                        .password(faker.bothify("??????"))
                        .build();

        return userRepository.saveAndFlush(customer);
    }
    @Transactional
    public User createSeller(){
        var customer=  User.builder()
                .email(faker.bothify("????##@gmail.com"))
                .userStatus(UserStatus.ACTIVE)
                .role(Role.SELLER)
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .password(faker.bothify("??????"))
                .build();
        return userRepository.saveAndFlush(customer);
    }
    @Transactional
    public Store createStore(){
        var store=Store.builder()
                .name(faker.app().name())
                .employees(new ArrayList<>())
                .orderStores(new HashSet<>())
                .build();
        return storeRepository.saveAndFlush(store);
    }
    @Transactional
    public StoreEmployee createStoreEmployeeWithUserAndStore(User user, Store store) {
        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setRole(Role.SELLER);
        user = userRepository.save(user);

        store = storeRepository.findById(store.getId())
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        StoreEmployee storeEmployee = StoreEmployee.builder()
                .employeeRole(EmployeeRole.ADMIN)
                .store(store)
                .user(user)
                .build();

        storeEmployee = storeEmployeeRepository.save(storeEmployee);

        store.getEmployees().add(storeEmployee);
        storeRepository.save(store);

        return storeEmployee;
    }
    @Transactional
    public Product createProductForStore(Store store){
        store = storeRepository.findById(store.getId())
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        var product=Product.builder()
                .id(UUID.randomUUID())
                .created_t(Instant.now())
                .price(faker.random().nextDouble())
                .description(faker.superhero().descriptor())
                .visible(true)
                .amountReturned(0)
                .amountSold(0)
                .store(store)
                .availableQuantity(100)
                .build();

        return productRepository.saveAndFlush(product);
    }


    @Transactional
    public void deleteAll(){
        productRepository.deleteAll();
        tokenRepository.deleteAll();
        storeEmployeeRepository.deleteAll();
        storeRepository.deleteAll();
        userRepository.deleteAll();
    }
}