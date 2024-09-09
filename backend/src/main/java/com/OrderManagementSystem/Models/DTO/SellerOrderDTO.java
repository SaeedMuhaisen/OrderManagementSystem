package com.OrderManagementSystem.Models.DTO;


import lombok.*;

import java.time.Instant;
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerOrderDTO {

    private String orderItemId;
    private String firstName;
    private String productId;
    private Instant orderDate;
    private String status;
}
