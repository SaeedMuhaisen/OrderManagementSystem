package com.OrderManagementSystem.CSR.Controllers;

import com.OrderManagementSystem.Entities.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate template;

    private Notifications notifications = new Notifications(0);

    @GetMapping("/notify")
    public String getNotification(String id) {
        notifications.increment();
        template.convertAndSend("/topic/notification/"+id, id);
        return "testing";
    }
}