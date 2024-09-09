package com.OrderManagementSystem.Models.DTO;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private String productId;
    private Integer quantity;
    private double productPrice;
    private String status;
}
