package com.OrderManagementSystem.CSR.Services;

import com.OrderManagementSystem.Entities.*;
import com.OrderManagementSystem.Entities.enums.EmployeeRole;
import com.OrderManagementSystem.Exceptions.StoreExceptions.UnAuthorizedEmployeeException;
import com.OrderManagementSystem.Mappers.OrderItemMapper;
import com.OrderManagementSystem.Mappers.SellerMapper;
import com.OrderManagementSystem.CSR.Repositories.*;
import com.OrderManagementSystem.Entities.enums.StatusType;
import com.OrderManagementSystem.Exceptions.AuthExceptions.UserNotFoundException;
import com.OrderManagementSystem.Exceptions.OrderExceptions.OrderStatusIllegalTransitionException;
import com.OrderManagementSystem.Models.DTO.*;
import com.OrderManagementSystem.Models.Notifications.NotificationMessage;
import com.OrderManagementSystem.Models.Notifications.NotificationType;
import com.OrderManagementSystem.Models.Notifications.UpdateStatusNotification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServices {
    private final StoreRepository storeRepository;

    public List<SellerDTO> getAllAvailableStores() {
        var stores=storeRepository.findAll();
        return SellerMapper.INSTANCE.sellerListToSellerDTOList(stores.stream().filter(store -> store.getProducts().size()>0).toList());
    }

}
