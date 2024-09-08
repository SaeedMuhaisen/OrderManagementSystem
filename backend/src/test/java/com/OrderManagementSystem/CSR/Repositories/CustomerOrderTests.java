package com.OrderManagementSystem.CSR.Repositories;



import com.OrderManagementSystem.CSR.Repositories.ProductRepository;
import com.OrderManagementSystem.CSR.Repositories.StoreEmployeeRepository;
import com.OrderManagementSystem.CSR.Repositories.StoreRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Entities.Product;
import com.OrderManagementSystem.Entities.Store;
import com.OrderManagementSystem.Entities.StoreEmployee;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Entities.enums.EmployeeRole;
import com.OrderManagementSystem.Entities.enums.Role;
import com.OrderManagementSystem.Entities.enums.UserStatus;
import com.github.javafaker.Faker;

import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;


@SpringBootTest
public class CustomerOrderTests {

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

    private final Faker faker=new Faker();


    private User createCustomer(){
        var customer=  User.builder()
                        .email(faker.bothify("????##@gmail.com"))
                        .userStatus(UserStatus.ACTIVE)
                        .role(Role.BUYER)
                        .firstname(faker.name().firstName())
                        .lastname(faker.name().lastName())
                        .password(faker.bothify("??????"))
                        .build();

        return userRepository.save(customer);
    }

    private User createSeller(){


        var customer=  User.builder()
                .email(faker.bothify("????##@gmail.com"))
                .userStatus(UserStatus.ACTIVE)
                .role(Role.SELLER)
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .password(faker.bothify("??????"))
                .build();

        return userRepository.save(customer);
    }

    private Store createStore(){
        var store=Store.builder()
                .name(faker.app().name())
                .build();

        return storeRepository.save(store);
    }

    private StoreEmployee createStoreEmployeeWithUserAndStore(User user, Store store){
        user.setRole(Role.SELLER);
        userRepository.save(user);
        var storeEmployee=StoreEmployee
                .builder()
                .employeeRole(EmployeeRole.ADMIN)
                .store(store)
                .user(user).build();
        return storeEmployeeRepository.save(storeEmployee);
    }

    private Product createProductForStore(Store store){
        var product=Product.builder()
                .created_t(Instant.now())
                .price(faker.random().nextDouble())
                .description(faker.superhero().descriptor())
                .visible(true)
                .amountReturned(0)
                .amountSold(0)
                .store(store)
                .availableQuantity(100)
                .build();
        return productRepository.save(product);
    }

    @Test
    public void testCreateMethods() {


        deleteAll();
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