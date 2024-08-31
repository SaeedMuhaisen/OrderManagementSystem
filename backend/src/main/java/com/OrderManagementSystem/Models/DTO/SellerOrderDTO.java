package com.OrderManagementSystem.Models.DTO;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Data
@Getter
@Setter
public class SellerOrderDTO {

    private String orderId;
    private String firstName;

    private String productId;
    private Instant orderDate;
    private String status;

}
