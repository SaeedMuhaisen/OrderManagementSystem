package com.OrderManagementSystem.CSR.Controllers;

import com.OrderManagementSystem.CSR.Services.OrderServices;
import com.OrderManagementSystem.CSR.Services.ProductServices;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/buyer")
@PreAuthorize("hasRole('BUYER')")
@AllArgsConstructor
public class BuyerController {

    private final ProductServices productServices;
    private final OrderServices orderServices;

    private static final Logger logger = LoggerFactory.getLogger(SellerController.class);

    @GetMapping("/v1/store/products")
    @PreAuthorize("hasAuthority('buyer:read')")
    public ResponseEntity<?> getAllProducts(){
        try{
            logger.info("getAllProducts() - init" );
            var products=productServices.getAllAvailableProducts();
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.info("getAllProducts() - failed error :{}", e.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/v1/store/order")
    @PreAuthorize("hasAuthority('buyer:read')")
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CreateOrderDTO createOrderDTO){
        try{
            logger.info("createOrder() - order received for item: {}",createOrderDTO.getProductId() );
            orderServices.createOrder(userDetails,createOrderDTO);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            logger.info("createOrder() - order for product: {} - failed error :{}", createOrderDTO.getProductId(),e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }
}
