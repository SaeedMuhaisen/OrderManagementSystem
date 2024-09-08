package com.OrderManagementSystem.Models.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreOrderDTO {
    private String orderId;
    private String orderDate;
    private String customerEmail;
//    private List<OrderItemDTO> orderItems;
}
