package com.OrderManagementSystem.Models.DTO;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderItemStatusDTO {
    private String orderItemId;
    private String status;
}
