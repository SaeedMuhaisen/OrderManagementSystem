package com.OrderManagementSystem.Entities;

import com.OrderManagementSystem.Models.Notifications.NotificationMessage;
import com.OrderManagementSystem.Models.Notifications.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    private String notificationMessage;

}
