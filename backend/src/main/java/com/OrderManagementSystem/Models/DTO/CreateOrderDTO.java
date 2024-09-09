package com.OrderManagementSystem.Models.DTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDTO {

    List<OrderItemDTO> products;
}
