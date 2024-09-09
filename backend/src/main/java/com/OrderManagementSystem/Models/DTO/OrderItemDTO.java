package com.OrderManagementSystem.Models.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderItemDTO {
    private String productId;
    private Integer quantity;
    private double productPrice;
    private String status;
}
