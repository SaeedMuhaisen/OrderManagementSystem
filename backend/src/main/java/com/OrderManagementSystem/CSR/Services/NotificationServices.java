package com.OrderManagementSystem.CSR.Services;

import com.OrderManagementSystem.CSR.Repositories.NotificationQueueRepository;
import com.OrderManagementSystem.CSR.Repositories.NotificationsRepository;
import com.OrderManagementSystem.CSR.Repositories.StoreEmployeeRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Entities.NotificationQueue;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Exceptions.AuthExceptions.UserNotFoundException;
import com.OrderManagementSystem.Models.DTO.StoreOrderDTO;
import com.OrderManagementSystem.Models.DTO.UpdateOrderItemStatusDTO;
import com.OrderManagementSystem.Models.Notifications.NotificationMessage;
import com.OrderManagementSystem.Models.Notifications.UpdateStatusNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServices {

    private final NotificationsRepository notificationsRepository;
    private final NotificationQueueRepository notificationQueueRepository;
    private final UserRepository userRepository;
    private final StoreEmployeeRepository storeEmployeeRepository;


    public boolean userIsConnected(User user){
        var sessions=notificationsRepository.findAllByUser(user);
        return !sessions.isEmpty();
    }

    public void queueMessage(User user, NotificationMessage notificationMessage) {
        try{
            notificationQueueRepository.save(NotificationQueue.builder()
                    .notificationMessage(notificationMessage.getMessage())
                    .user(user)
                    .build());
        }catch (Exception e){
            System.out.println("FAILED TO STRINGIFY!!"+ e.getMessage());
        }

    }

    public List<StoreOrderDTO> fetchStoreNotifications(UserDetails userDetails){
        var user= userRepository.findById(((User) userDetails).getId());
        var storeEmployee= storeEmployeeRepository.findByUser(user.get());

        if(storeEmployee.isEmpty()){
            throw new UserNotFoundException("User is not connected with any store");
        }

        var notifications = notificationQueueRepository.findAllByUser(user.get());
        var messages = notifications.stream()
                .map(NotificationQueue::getNotificationMessage)
                .toList();
        var objectMapper = new ObjectMapper();
        var mappedMessages = messages.stream()
                .map(item -> {
                    try {
                       return objectMapper.readValue(item, StoreOrderDTO.class);
                    } catch (JsonProcessingException e) {
                        System.out.println("Failed to parse JSON" + e.getMessage()+ e);
                        return null;
                    }
                })
                .toList();
        return mappedMessages;
    }

    public List<UpdateStatusNotification> fetchCustomerNotifications(UserDetails userDetails){
        var user= userRepository.findById(((User) userDetails).getId());
        if(user.isEmpty()){
            throw new UserNotFoundException("User couldn't be found");
        }

        var notifications = notificationQueueRepository.findAllByUser(user.get());
        var messages = notifications.stream()
                .map(NotificationQueue::getNotificationMessage)
                .toList();
        var objectMapper = new ObjectMapper();
        return messages.stream()
                .map(item -> {
                    try {
                        return objectMapper.readValue(item, UpdateStatusNotification.class);
                    } catch (JsonProcessingException e) {
                        System.out.println("Failed to parse JSON" + e.getMessage()+ e);
                        return null;
                    }
                })
                .toList();
    }
    @Transactional
    public void deleteBySessionId(String sessionId) {
        try{

            notificationsRepository.deleteBySessionId(sessionId);
        }catch (Exception e){
            System.out.println("Something went worng:::"+e.getMessage());
        }
    }
}
