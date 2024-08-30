package com.OrderManagementSystem.Models.DTO;

import com.OrderManagementSystem.Entities.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Data
@Getter
@Setter
@Builder
public class ProductDTO {
    public String id;
    public String name;
    public String description;
    public Double price;
    public Instant created_t;
    public Integer availableQuantity;
    public Integer amountSold;
    public Integer amountReturned;
    public boolean visible;
}
