package com.OrderManagementSystem.Models.Authentication;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerRegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String storeName;
}
