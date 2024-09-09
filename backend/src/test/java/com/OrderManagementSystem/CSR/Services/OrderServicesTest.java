package com.OrderManagementSystem.CSR.Services;

import com.OrderManagementSystem.CSR.Repositories.*;
import com.OrderManagementSystem.Entities.*;
import com.OrderManagementSystem.Entities.enums.StatusType;
import com.OrderManagementSystem.Exceptions.OrderExceptions.ProductQuantityNotEnoughException;
import com.OrderManagementSystem.Exceptions.StoreExceptions.UnAuthorizedEmployeeException;
import com.OrderManagementSystem.Models.DTO.CreateOrderDTO;
import com.OrderManagementSystem.Models.DTO.CreateProductDTO;
import com.OrderManagementSystem.Models.DTO.OrderItemDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OrderServicesTest {
    @Autowired
    private CustomerOrderTestHelper utils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    StoreEmployeeRepository storeEmployeeRepository;
    @Autowired
    OrderServices orderServices;

    @BeforeEach
    void setUp() {
        utils.deleteAll();
    }

    @AfterEach
    void tearDown() {
        utils.deleteAll();
    }

    @Test
    void createOrderTest() throws JsonProcessingException {

        var createUser = utils.createCustomer();
        var createStore=utils.createStore();
        var createProduct=utils.createProductForStore(createStore);

        OrderItemDTO orderItemDTO =
                OrderItemDTO
                        .builder()
                        .productId(String.valueOf(createProduct.getId()))
                        .quantity(createProduct.getAvailableQuantity()+100)
                        .build();
        var createOrderDTO = CreateOrderDTO
                .builder()
                .products(List.of(orderItemDTO))
                .build();



        assertThrows(ProductQuantityNotEnoughException.class,()-> {
            orderServices.createOrder(createUser, createOrderDTO);
        });

        orderItemDTO.setQuantity(createProduct.getAvailableQuantity());
        orderServices.createOrder(createUser, createOrderDTO);


        var orders = orderRepository.findAll();
        assert(orders.size()==1);

        var order = orders.get(0);
        assert(createUser.getId().equals(order.getBuyer().getId()));
        assert(order.getOrderItems().get(0).getProduct().getId().equals(createProduct.getId()));
        assert(Objects.equals(order.getOrderItems().get(0).getQuantity(), createProduct.getAvailableQuantity()));

        //we check if the quantity of the product has been updated:
        var product=productRepository.findById(createProduct.getId());
        assert(product.isPresent());
        var productQuantity=product.get().getAvailableQuantity();
        assert(productQuantity==0);
    }

    @Test
    void getCustomerActiveOrdersTest() {
        var createUser = utils.createCustomer();
        var createStore=utils.createStore();
        var createProduct=utils.createProductForStore(createStore);

        var orderItem= OrderItem
                .builder()
                .product(createProduct)
                .productPrice(createProduct.getPrice())
                .statusType(StatusType.PENDING)
                .quantity(1)
                .build();
        var createOrder=Order.builder()
                .created_t(Instant.now())
                .orderItems(List.of(orderItem))
                .buyer(createUser)
                .build();
        orderItem.setOrder(createOrder);
        var orderStore=OrderStore
                .builder()
                .store(createStore)
                .order(createOrder)
                .finished(false)
                .build();
        createOrder.setOrderStores(Set.of(orderStore));
        createOrder = orderRepository.save(createOrder);


        var order=orderRepository.findById(createOrder.getId());
        assert(order.isPresent());


        var activeOrders = orderServices.getCustomerActiveOrders(createUser);

        // Verify results
        assert(activeOrders.size()==1);
        assert(activeOrders.get(0).getOrderItems().size()==1);
        assert(activeOrders
                .get(0)
                .getOrderItems()
                .stream()
                .toList()
                .get(0)
                .getProductId()
                .equals(String.valueOf(orderItem.getProduct().getId()))
        );
    }

    @Test
    void getStoreActiveOrdersTest() {
        var createUser = utils.createCustomer();
        var createSeller = utils.createCustomer();
        var createStore=utils.createStore();
        var createProduct=utils.createProductForStore(createStore);

        utils.createStoreEmployeeWithUserAndStore(createSeller,createStore);

        var orderItem= OrderItem
                .builder()
                .product(createProduct)
                .productPrice(createProduct.getPrice())
                .statusType(StatusType.PENDING)
                .quantity(1)
                .build();
        var createOrder=Order.builder()
                .created_t(Instant.now())
                .orderItems(List.of(orderItem))
                .buyer(createUser)
                .build();
        orderItem.setOrder(createOrder);
        var orderStore=OrderStore
                .builder()
                .store(createStore)
                .order(createOrder)
                .finished(false)
                .build();
        createOrder.setOrderStores(Set.of(orderStore));
        createOrder = orderRepository.save(createOrder);


        var order=orderRepository.findById(createOrder.getId());
        assert(order.isPresent());

        assertThrows(UnAuthorizedEmployeeException.class,()->{orderServices.getStoreActiveOrders(createUser);});
        var activeStoreOrders =orderServices.getStoreActiveOrders(createSeller);

        assert(activeStoreOrders.size()==1);
        assert(activeStoreOrders.get(0).getOrderId().equals(String.valueOf(order.get().getId())));
    }
}