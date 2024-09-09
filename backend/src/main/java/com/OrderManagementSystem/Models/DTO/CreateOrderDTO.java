package com.OrderManagementSystem.Models.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@Builder
public class CreateOrderDTO {

    List<OrderItemDTO> products;
}
