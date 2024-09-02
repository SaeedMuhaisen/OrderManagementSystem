package com.OrderManagementSystem.Models.DTO;

import com.OrderManagementSystem.Entities.OrderItem;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Getter
@Setter
public class BuyerOrderDTO {

    private String orderId;
    private Instant orderDate;
    private List<OrderItemDTO> orderItems;

}
