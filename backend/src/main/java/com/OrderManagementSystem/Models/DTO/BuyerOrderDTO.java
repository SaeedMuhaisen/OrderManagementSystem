package com.OrderManagementSystem.Models.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Data
@Getter
@Setter
public class BuyerOrderDTO {

    private String orderId;
    private String sellerEmail;
    private String productId;
    private Instant orderDate;
    private String status;
}
