package com.OrderManagementSystem.Models.Notifications;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.io.Serializable;

@Getter
@Setter
@Builder

public class NotificationMessage implements Serializable {
    private NotificationType notificationType;
    private String message;
}
