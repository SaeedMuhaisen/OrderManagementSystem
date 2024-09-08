package com.OrderManagementSystem.Models.Notifications;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusNotification {
    private String orderId;
    private String productId;
    private String newStatus;
}
