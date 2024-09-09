package com.OrderManagementSystem.Models.DTO;

import com.OrderManagementSystem.Entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String id;
    private String name;
    private String description;
    private Double price;
    private Instant created_t;
    private Integer availableQuantity;
    private Integer amountSold;
    private Integer amountReturned;
    private boolean visible;
}
