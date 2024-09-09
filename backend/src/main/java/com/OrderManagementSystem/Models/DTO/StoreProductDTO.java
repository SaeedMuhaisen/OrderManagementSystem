package com.OrderManagementSystem.Models.DTO;

import lombok.*;

import java.time.Instant;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductDTO {
    public String id;
    public String name;
    public String description;
    public Double price;
    public Integer availableQuantity;
}