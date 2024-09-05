package com.OrderManagementSystem.Models.DTO;

import com.OrderManagementSystem.Entities.OrderItem;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
public class BuyerOrderDTO {

    private String orderId;
    private Instant orderDate;
    private Set<OrderItemDTO> orderItems;

}
