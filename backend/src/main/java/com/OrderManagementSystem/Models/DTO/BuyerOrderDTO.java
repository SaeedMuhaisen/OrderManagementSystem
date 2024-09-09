package com.OrderManagementSystem.Models.DTO;

import com.OrderManagementSystem.Entities.OrderItem;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyerOrderDTO {

    private String orderId;
    private Instant orderDate;
    private Set<OrderItemDTO> orderItems;

}
