package com.OrderManagementSystem.CSR.Services;

import Mappers.OrderItemMapper;
import Mappers.ProductMapper;
import Mappers.SellerMapper;
import com.OrderManagementSystem.CSR.Repositories.*;
import com.OrderManagementSystem.Entities.Store;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Entities.enums.StatusType;
import com.OrderManagementSystem.Exceptions.AuthExceptions.UserNotFoundException;
import com.OrderManagementSystem.Exceptions.OrderExceptions.OrderStatusIllegalTransitionException;
import com.OrderManagementSystem.Models.DTO.ProductDTO;
import com.OrderManagementSystem.Models.DTO.SellerDTO;
import com.OrderManagementSystem.Models.DTO.SellerOrderDTO;
import com.OrderManagementSystem.Models.DTO.UpdateOrderItemStatusDTO;
import com.OrderManagementSystem.Models.Notifications.NotificationMessage;
import com.OrderManagementSystem.Models.Notifications.NotificationType;
import com.OrderManagementSystem.Models.Notifications.UpdateStatusNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreServices {
    private final StoreRepository storeRepository;
    private final StoreEmployeeRepository storeEmployeeRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final SimpMessagingTemplate template;

    public List<SellerDTO> getAllAvailableStores() {
        var stores=storeRepository.findAll();
        return SellerMapper.INSTANCE.sellerListToSellerDTOList(stores.stream().filter(store -> store.getProducts().size()>0).toList());
    }

    public List<ProductDTO> getStoreProducts(String sellersId) {
        var store   = storeRepository.getReferenceById(UUID.fromString(sellersId));
        return ProductMapper.INSTANCE.productListToProductDTOList(store.getProducts().stream().filter(product -> product.isVisible()).toList());
    }

    public List<ProductDTO> getStoreProductsByEmployee(UserDetails userDetails) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());
        var storeEmployee= storeEmployeeRepository.findByUser(user);

        if(storeEmployee.isEmpty()){
            throw new UserNotFoundException("User is not connected with any store");
        }
        return ProductMapper.INSTANCE.productListToProductDTOList(storeEmployee.get().getStore().getProducts());
    }

    public List<SellerOrderDTO> getAllStoreOrders(UserDetails userDetails) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());
        var storeEmployee= storeEmployeeRepository.findByUser(user);

        if(storeEmployee.isEmpty()){
            throw new UserNotFoundException("User is not connected with any store");
        }

        return OrderItemMapper.INSTANCE.orderItemListToSellerOrderDTOList(storeEmployee.get().getStore().getOrderItems());
    }


    public void updateOrderItemStatus(UserDetails userDetails, UpdateOrderItemStatusDTO updateOrderItemStatusDTO) throws AccessDeniedException {
        var user = userRepository.getReferenceById(((User) userDetails).getId());
        var storeEmployee = storeEmployeeRepository.findByUser(user);

        if (storeEmployee.isEmpty()) {
            throw new UserNotFoundException("User is not connected with any store");
        }

        var orderItem = orderItemRepository.findById(UUID.fromString(updateOrderItemStatusDTO.getOrderItemId()));
        if (!orderItem.isPresent() || !orderItem.get().getStore().getId().equals(storeEmployee.get().getStore().getId())) {
            throw new AccessDeniedException("Oder item doesnt exist or doesnt belong to employee's store");
        }

        var oldStatus = orderItem.get().getStatusType();
        var newStatus = StatusType.valueOf(updateOrderItemStatusDTO.getStatus());


        if (StatusType.ACCEPTED.isIllegalStatusTransition(oldStatus, newStatus)) {
            throw new OrderStatusIllegalTransitionException("Invalid status transition. oldStatus: " + oldStatus + ", newStatus: " + newStatus);
        }

        orderItem.get().setStatusType(newStatus);
        orderItemRepository.save(orderItem.get());

        var buyerId=orderItem.get().getOrder().getBuyer().getId();

        template.convertAndSend(
                "/topic/notification/" + buyerId,
                NotificationMessage
                        .builder()
                        .notificationType(NotificationType.BUYER_UPDATE_ORDER_STATUS)
                        .message(UpdateStatusNotification
                                .builder()
                                .orderId(String.valueOf(orderItem.get().getOrder().getId()))
                                .productId(String.valueOf(orderItem.get().getProduct().getId()))
                                .newStatus(newStatus.name()).build())
                        .build()
        );

    }
}
