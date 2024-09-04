package com.OrderManagementSystem.CSR.Controllers;

import com.OrderManagementSystem.CSR.Services.OrderServices;
import com.OrderManagementSystem.CSR.Services.ProductServices;
import com.OrderManagementSystem.CSR.Services.StoreServices;
import com.OrderManagementSystem.Models.DTO.CreateProductDTO;

import com.OrderManagementSystem.Models.DTO.SellerOrderDTO;
import com.OrderManagementSystem.Models.DTO.UpdateOrderItemStatusDTO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/seller")
@PreAuthorize("hasRole('SELLER')")
@AllArgsConstructor
public class SellerController {

    private final ProductServices productServices;
    private final StoreServices storeServices;
    private static final Logger logger = LoggerFactory.getLogger(SellerController.class);

    @PostMapping("/v1/product")
    @PreAuthorize("hasAuthority('seller:create')")
    public ResponseEntity<?> createProduct(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CreateProductDTO createProductDTO){
        try{
            logger.info("createProduct() - seller creating new product : ${}", createProductDTO);
            productServices.createProduct(userDetails,createProductDTO);

            return ResponseEntity.ok().build();
        }catch (Exception e){
            logger.info("createProduct() - failed error :{}", e.getMessage());
            return ResponseEntity.ok().build();
        }

    }

    @GetMapping("/v1/products")
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<?> getStoreProductsByEmployee(@AuthenticationPrincipal UserDetails userDetails){
        try{
            logger.info("getAllProducts() - user fetching all products : ${}",userDetails.getUsername() );
            var products=storeServices.getStoreProductsByEmployee(userDetails);
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.info("getAllProducts() - failed error :{}", e.getMessage());
            return ResponseEntity.ok().build();
        }
    }
    @GetMapping("/v1/orders")
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<?> getStoreOrders(@AuthenticationPrincipal UserDetails userDetails){
        try{
            logger.info("getAllOrders() - Seller fetching all orders : ${}",userDetails.getUsername() );
            var products=storeServices.getAllStoreOrders(userDetails);
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.info("getAllProducts() - failed error :{}", e.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/v1/orders/{orderId}")
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<?> getOrderItemFromOrderId(@AuthenticationPrincipal UserDetails userDetails,@PathVariable String orderId){
        try{
            logger.info("getOrderItemFromOrderId() - employee fetching order details of order : ${}",orderId );
            var products=storeServices.getOrderItemFromOrderId(userDetails,orderId);
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.info("getOrderItemFromOrderId() - failed error :{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PutMapping("/v1/order/status")
    @PreAuthorize("hasAuthority('seller:update')")
    public ResponseEntity<?> updateOrderStatus(@AuthenticationPrincipal UserDetails userDetails,@RequestBody UpdateOrderItemStatusDTO updateOrderItemStatusDTO){
        try{
            logger.info("updateOrderStatus() - Seller updating order Status : ${}",userDetails.getUsername() );
            storeServices.updateOrderItemStatus(userDetails, updateOrderItemStatusDTO);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            logger.info("updateOrderStatus() - failed error :{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @GetMapping("/v1/orders/history")
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<?> getStoreOrderHistory(@AuthenticationPrincipal UserDetails userDetails){
        try{
            logger.info("getOrderHistory() - Seller fetching order history: ${}",userDetails.getUsername() );
            var products=storeServices.getStoreOrderHistory(userDetails);
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.info("getOrderHistory() - failed error :{}", e.getMessage());
            return ResponseEntity.ok().build();
        }
    }
}
