package com.OrderManagementSystem.Models.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class CreateOrderDTO {

    List<OrderItemDTO> products;
}
