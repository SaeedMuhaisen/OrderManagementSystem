package com.OrderManagementSystem.CSR.Controllers;

import com.OrderManagementSystem.CSR.Services.*;
import com.OrderManagementSystem.Models.DTO.CreateProductDTO;

import com.OrderManagementSystem.Models.DTO.ProductDTO;
import com.OrderManagementSystem.Models.DTO.UpdateOrderItemStatusDTO;
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


@RestController
@RequestMapping("/api/seller")
@PreAuthorize("hasRole('SELLER')")
@AllArgsConstructor
public class StoreController {

    private final ProductServices productServices;
    private final OrderServices orderServices;
    private final OrderItemServices orderItemServices;
    private final OrderHistoryServices orderHistoryServices;
    private final OrderItemHistoryServices orderItemHistoryServices;
    private final NotificationServices notificationServices;

    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

    @PostMapping("/v1/product")
    @PreAuthorize("hasAuthority('seller:create')")
    public ResponseEntity<?> createStoreProduct(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CreateProductDTO createProductDTO){
        try{
            logger.info("createStoreProduct() - Store employee creating a new product : {}", createProductDTO);
            productServices.createStoreProduct(userDetails,createProductDTO);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            logger.error("createStoreProduct() - failed to create product:{}, error :{}",createProductDTO, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @GetMapping("/v1/products")
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<?> getStoreProductsByEmployee(@AuthenticationPrincipal UserDetails userDetails){
        try{
            logger.info("getStoreProductsByEmployee() - store employee fetching all store products : ${}",userDetails.getUsername() );
            var products=productServices.getStoreProductsByEmployee(userDetails);
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.error("getStoreProductsByEmployee() - failed to get store products by employee, error :{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }
    @GetMapping("/v1/orders")
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<?> getStoreActiveOrders(@AuthenticationPrincipal UserDetails userDetails){
        try{
            logger.info("getStoreActiveOrders() - Store employee fetching all active orders : ${}",userDetails.getUsername() );
            var products=   orderServices.getStoreActiveOrders(userDetails);
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.error("getStoreActiveOrders() - failed to fetch store active orders username:{}, error :{}",userDetails.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @GetMapping("/v1/order/{orderId}")
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<?> getOrderItemsByOrderId(@AuthenticationPrincipal UserDetails userDetails,@PathVariable String orderId){
        try{
            logger.info("getOrderItemsByOrderId() - store employee fetching order items of orderId: {}" ,orderId );
            var products=orderItemServices.getOrderItemsByOrderId(userDetails,orderId);
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.error("getOrderItemsByOrderId() - failed to fetch orderItems for order:{}, error :{}",orderId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }


    @PutMapping("/v1/order/status")
    @PreAuthorize("hasAuthority('seller:update')")
    public ResponseEntity<?> updateOrderItemStatus(@AuthenticationPrincipal UserDetails userDetails,@RequestBody UpdateOrderItemStatusDTO updateOrderItemStatusDTO){
        try{
            logger.info("updateOrderStatus() - employee updating order item status - request body:{}",updateOrderItemStatusDTO );
            orderItemServices.updateOrderItemStatus(userDetails, updateOrderItemStatusDTO);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            logger.error("updateOrderStatus() - failed to update order item status, request was :{} , error  :{}",updateOrderItemStatusDTO, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @GetMapping("/v1/orders/history")
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<?> getStoreOrderHistory(@AuthenticationPrincipal UserDetails userDetails){
        try{
            logger.info("getStoreOrderHistory() - Store employee fetching order history: username: {}",userDetails.getUsername() );
            var products=orderHistoryServices.getStoreOrderHistory(userDetails);
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.error("getStoreOrderHistory() - failed to fetch store order history, username:{}, error :{}",userDetails.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @GetMapping("/v1/order/history/{orderHistoryId}")
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<?> getOrderItemHistoryByOrderHistoryId(@AuthenticationPrincipal UserDetails userDetails,@PathVariable String orderHistoryId){
        try{
            logger.info("getOrderItemHistoryByOrderHistoryId() - Store employee fetching order item history of orderHistoryId: ${}",orderHistoryId );
            var products=orderItemHistoryServices.getOrderItemHistoryByOrderHistoryId(userDetails,orderHistoryId);
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.error("getOrderItemHistoryByOrderHistoryId() - failed to fetch items of order history, orderHistoryId:{}, error :{}",orderHistoryId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @GetMapping("v1/notifications")
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<?> fetchStoreNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            logger.info("fetchStoreNotifications() - employee is fetching queued notifications: user:{}", userDetails.getUsername());
            var messages = notificationServices.fetchStoreNotifications(userDetails);
            return ResponseEntity.ok().body(messages);
        } catch (Exception e) {
            logger.error("fetchStoreNotifications() - failed to fetch notifications, user:{}, error:{}", userDetails.getUsername(),e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }
    @PutMapping("/v1/product")
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<?> updateStoreProduct(@AuthenticationPrincipal UserDetails userDetails,@RequestBody CreateProductDTO newProductDTO) {
        try {
            logger.info("updateStoreProduct() - employee is updating a product : user:{}", userDetails.getUsername());
            productServices.updateStoreProduct(userDetails,newProductDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("updateStoreProduct() - failed to update product , user:{}, error:{}", userDetails.getUsername(),e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }
}
