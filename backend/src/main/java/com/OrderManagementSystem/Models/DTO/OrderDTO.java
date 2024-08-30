package com.OrderManagementSystem.Models.DTO;


import com.OrderManagementSystem.Entities.Product;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Entities.enums.StatusType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Data
@Getter
@Setter
public class OrderDTO {

    private String orderId;
    private String firstName;

    private String productId;
    private Instant orderDate;
    private String status;

}
