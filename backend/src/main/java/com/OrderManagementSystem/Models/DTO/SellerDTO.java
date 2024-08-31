package com.OrderManagementSystem.Models.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class SellerDTO {
    private String sellerName;
    private String sellerId;
}
