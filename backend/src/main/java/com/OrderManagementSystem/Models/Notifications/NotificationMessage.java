package com.OrderManagementSystem.Models.Notifications;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
@Builder
public class NotificationMessage {
    private NotificationType notificationType;
    private Object message;
}
