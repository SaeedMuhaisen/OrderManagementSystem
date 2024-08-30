package com.OrderManagementSystem.Models.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Data
@Getter
@Setter
@Builder
public class StoreProductDTO {
    public String id;
    public String name;
    public String description;
    public Double price;
    public Integer availableQuantity;
}