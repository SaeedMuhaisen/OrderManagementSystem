package com.OrderManagementSystem.CSR.Services;

import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Models.Notifications.NotificationMessage;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class MessageBrokerServices {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationServices notificationServices;
    public void sendNotification(String destination, User user, NotificationMessage notificationMessage){
        if(notificationServices.userIsConnected(user)){
            simpMessagingTemplate.convertAndSend(destination, notificationMessage);
            notificationServices.queueMessage(user,notificationMessage);
        }else{
            notificationServices.queueMessage(user,notificationMessage);
        }
    }
}
