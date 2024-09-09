package com.OrderManagementSystem.Models.DTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreOrderDTO {
    private String orderId;
    private String orderDate;
    private String customerEmail;
//    private List<OrderItemDTO> orderItems;
}
