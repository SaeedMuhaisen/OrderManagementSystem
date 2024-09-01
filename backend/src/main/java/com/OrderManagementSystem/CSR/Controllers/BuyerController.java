package com.OrderManagementSystem.CSR.Controllers;

import com.OrderManagementSystem.CSR.Services.OrderServices;
import com.OrderManagementSystem.CSR.Services.ProductServices;
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
@RequestMapping("/api/buyer")
@PreAuthorize("hasRole('BUYER')")
@AllArgsConstructor
public class BuyerController {

    private final ProductServices productServices;
    private final StoreServices storeServices;
    private final OrderServices orderServices;

    private static final Logger logger = LoggerFactory.getLogger(SellerController.class);


    @GetMapping("v1/stores")
    @PreAuthorize("hasAuthority('buyer:read')")
    public ResponseEntity<?> getAllAvailableStores(){
        try{
            logger.info("getAllAvailableSellers() - init" );
            var stores=storeServices.getAllAvailableStores();
            return ResponseEntity.ok().body(stores);
        }catch (Exception e){
            logger.info("getAllAvailableSellers() - failed error :{}", e.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/v1/store/products")
    @PreAuthorize("hasAuthority('buyer:read')")
    public ResponseEntity<?> getSellerProducts(@RequestBody String sellerId){
        try{
            logger.info("getSellerStore() - init" );
            var products=productServices.getAllProductsBySellersId(sellerId);
            return ResponseEntity.ok().body(products);
        }catch (Exception e){
            logger.info("getAllAvailableSellers() - failed error :{}", e.getMessage());
            return ResponseEntity.ok().build();
        }
    }

//    @GetMapping("/v1/store/products")
//    @PreAuthorize("hasAuthority('buyer:read')")
//    public ResponseEntity<?> getAllProducts(){
//        try{
//            logger.info("getAllProducts() - init" );
//            var products=productServices.getAllAvailableProducts();
//            return ResponseEntity.ok().body(products);
//        }catch (Exception e){
//            logger.info("getAllProducts() - failed error :{}", e.getMessage());
//            return ResponseEntity.ok().build();
//        }
//    }

    @PostMapping("/v1/store/order")
    @PreAuthorize("hasAuthority('buyer:read')")
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

    @GetMapping("v1/orders")
    @PreAuthorize("hasAuthority('buyer:read')")
    public ResponseEntity<?> getAllBuyerOrders(@AuthenticationPrincipal UserDetails userDetails){
        try{
            logger.info("getAllBuyerOrders() - buyer fetching his orders ,username: {}",userDetails.getUsername());
            var orders=orderServices.getAllBuyerOrders(userDetails);
            return ResponseEntity.ok().body(orders);
        }catch (Exception e){
            logger.info("getAllBuyerOrders() - failed error :{}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
