package com.OrderManagementSystem.Models.Notifications;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateStatusNotification {
    private String orderId;
    private String productId;
    private String newStatus;
}
