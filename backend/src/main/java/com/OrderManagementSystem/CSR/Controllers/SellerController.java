package com.OrderManagementSystem.CSR.Controllers;

import com.OrderManagementSystem.CSR.Services.OrderServices;
import com.OrderManagementSystem.CSR.Services.ProductServices;
import com.OrderManagementSystem.Models.DTO.CreateProductDTO;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/seller")
@PreAuthorize("hasRole('SELLER')")
@AllArgsConstructor
public class SellerController {

    private final ProductServices productServices;
    private final OrderServices orderServices;

    private static final Logger logger = LoggerFactory.getLogger(SellerController.class);

    @PostMapping("/v1/create")
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

    private ResponseEntity<?> updateProduct(){
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> deleteProduct(){
        return ResponseEntity.ok().build();
    }
    @GetMapping("/v1/products/all")
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<?> getAllProducts(@AuthenticationPrincipal UserDetails userDetails){
        try{
            logger.info("getAllProducts() - user fetching all products : ${}",userDetails.getUsername() );
            var products=productServices.getAllProductsBySeller(userDetails);
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.info("getAllProducts() - failed error :{}", e.getMessage());
            return ResponseEntity.ok().build();
        }
    }
    @GetMapping("/v1/orders/all")
    @PreAuthorize("hasAuthority('seller:read')")
    public ResponseEntity<?> getAllOrders(@AuthenticationPrincipal UserDetails userDetails){
        try{
            logger.info("getAllOrders() - Seller fetching all orders : ${}",userDetails.getUsername() );
            var products=orderServices.getAllOrders(userDetails);
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.info("getAllProducts() - failed error :{}", e.getMessage());
            return ResponseEntity.ok().build();
        }
    }
}
