package com.OrderManagementSystem.Models.Notifications;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusNotification {
    private String orderId;
    private String productId;
    private String newStatus;
}
