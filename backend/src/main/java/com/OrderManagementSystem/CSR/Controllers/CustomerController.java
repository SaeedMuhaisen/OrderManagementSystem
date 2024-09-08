package com.OrderManagementSystem.CSR.Controllers;

import com.OrderManagementSystem.CSR.Services.NotificationServices;
import com.OrderManagementSystem.CSR.Services.OrderServices;
import com.OrderManagementSystem.CSR.Services.StoreServices;
import com.OrderManagementSystem.Models.DTO.CreateOrderDTO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/api/buyer")
@PreAuthorize("hasRole('BUYER')")
@AllArgsConstructor
public class CustomerController {

    private final StoreServices storeServices;
    private final OrderServices orderServices;
    private final NotificationServices notificationServices;

    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);


    @GetMapping("v1/stores")
    @PreAuthorize("hasAuthority('buyer:read')")
    public ResponseEntity<?> getAllAvailableStores(){
        try{
            logger.info("getAllAvailableSellers() - init" );
            var stores=storeServices.getAllAvailableStores();
            return ResponseEntity.ok().body(stores);
        }catch (Exception e){
            logger.info("getAllAvailableSellers() - failed error :{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v1/store/{storeId}")
    @PreAuthorize("hasAuthority('buyer:read')")
    public ResponseEntity<?> getStoreProducts(@PathVariable String storeId){
        try{
            logger.info("getStoreProducts() - storeId: {}", storeId);
            var products=storeServices.getStoreProducts(storeId);
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.error("getStoreProducts() - failed storeId:{}, error :{}", storeId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("v1/orders")
    @PreAuthorize("hasAuthority('buyer:read')")
    public ResponseEntity<?> getCustomerActiveOrders(@AuthenticationPrincipal UserDetails userDetails){
        try{
            logger.info("getCustomerActiveOrders() - username: {}",userDetails.getUsername());
            var orders=orderServices.getCustomerActiveOrders(userDetails);
            return ResponseEntity.ok().body(orders);
        }catch (Exception e){
            logger.info("getCustomerActiveOrders() - failed error :{}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("v1/orders/history")
    @PreAuthorize("hasAuthority('buyer:read')")
    public ResponseEntity<?> getCustomerOrdersHistory(@AuthenticationPrincipal UserDetails userDetails){
        try{
            logger.info("getAllBuyerOrders() - buyer fetching his orders ,username: {}",userDetails.getUsername());
            var orders=orderServices.getCustomerOrdersHistory(userDetails);
            return ResponseEntity.ok().body(orders);
        }catch (Exception e){
            logger.info("getAllBuyerOrders() - failed error :{}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("v1/order")
    @PreAuthorize("hasAuthority('buyer:create')")
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CreateOrderDTO createOrderDTO){
        try{
            logger.info("createOrder() - order received for items: {}",createOrderDTO.getProducts() );
            orderServices.createOrder(userDetails,createOrderDTO);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            logger.info("createOrder() - order for product: {} - failed error :{}", createOrderDTO.getProducts(),e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @GetMapping("v1/notifications")
    @PreAuthorize("hasAuthority('buyer:read')")
    public ResponseEntity<?> fetchCustomerNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            logger.info("fetchCustomerNotifications() - customer is fetching queued notifications: user:{}", userDetails.getUsername());
            var messages = notificationServices.fetchCustomerNotifications(userDetails);
            return ResponseEntity.ok().body(messages);
        } catch (Exception e) {
            logger.info("fetchCustomerNotifications() - failed to fetch notifications, user:{}", userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

}
