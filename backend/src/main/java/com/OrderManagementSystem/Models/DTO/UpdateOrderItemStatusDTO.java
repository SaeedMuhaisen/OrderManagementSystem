package com.OrderManagementSystem.Models.DTO;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderItemStatusDTO {
    private String orderItemId;
    private String status;
}
