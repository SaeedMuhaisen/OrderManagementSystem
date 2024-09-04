package com.OrderManagementSystem.Models.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@Data
public class StoreOrderDTO {
    private String orderId;
    private String orderDate;
    private String customerEmail;
//    private List<OrderItemDTO> orderItems;
}
