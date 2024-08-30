package com.OrderManagementSystem.CSR.Controllers;

import com.OrderManagementSystem.CSR.Services.ProductServices;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/buyer")
@PreAuthorize("hasRole('BUYER')")
@AllArgsConstructor
public class BuyerController {

    private final ProductServices productServices;

    private static final Logger logger = LoggerFactory.getLogger(SellerController.class);

    @GetMapping("/v1/store/products")
    @PreAuthorize("hasAuthority('buyer:read')")
    public ResponseEntity<?> getAllProducts(){
        try{
            logger.info("getAllProducts() - init" );
            var products=productServices.getAllProducts();
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.info("getAllProducts() - failed error :{}", e.getMessage());
            return ResponseEntity.ok().build();
        }
    }
}
