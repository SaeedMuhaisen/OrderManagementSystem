package com.OrderManagementSystem.Models.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateOrderDTO {
    String productId;
    Integer quantity;
}
