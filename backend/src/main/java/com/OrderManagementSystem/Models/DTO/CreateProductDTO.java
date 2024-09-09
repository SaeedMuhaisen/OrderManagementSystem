package com.OrderManagementSystem.Models.DTO;

import com.OrderManagementSystem.Entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDTO {
    private String id;
    private String name;
    private String description;
    private Double price;
    private Integer availableQuantity;
    private boolean visible;
}
