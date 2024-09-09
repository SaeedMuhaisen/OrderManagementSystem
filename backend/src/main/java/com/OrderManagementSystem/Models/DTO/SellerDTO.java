package com.OrderManagementSystem.Models.DTO;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerDTO {
    private String sellerName;
    private String sellerId;
}
