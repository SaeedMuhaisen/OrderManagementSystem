package com.OrderManagementSystem.CSR.Services;

import com.OrderManagementSystem.Entities.*;
import com.OrderManagementSystem.Exceptions.StoreExceptions.UnAuthorizedEmployeeException;
import com.OrderManagementSystem.Mappers.OrderItemMapper;
import com.OrderManagementSystem.Mappers.ProductMapper;
import com.OrderManagementSystem.Mappers.SellerMapper;
import com.OrderManagementSystem.CSR.Repositories.*;
import com.OrderManagementSystem.Entities.enums.StatusType;
import com.OrderManagementSystem.Exceptions.AuthExceptions.UserNotFoundException;
import com.OrderManagementSystem.Exceptions.OrderExceptions.OrderStatusIllegalTransitionException;
import com.OrderManagementSystem.Mappers.StoreMapper;
import com.OrderManagementSystem.Models.DTO.*;
import com.OrderManagementSystem.Models.Notifications.NotificationMessage;
import com.OrderManagementSystem.Models.Notifications.NotificationType;
import com.OrderManagementSystem.Models.Notifications.UpdateStatusNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
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
    private final OrderRepository orderRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderItemHistoryRepository orderItemHistoryRepository;

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

    public List<StoreOrderDTO> getAllStoreOrders(UserDetails userDetails) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());
        var storeEmployee= storeEmployeeRepository.findByUser(user);

        if(storeEmployee.isEmpty()){
            throw new UserNotFoundException("User is not connected with any store");
        }

        var orders=orderRepository.findAllByStoreId(storeEmployee.get().getStore().getId());
        return StoreMapper.INSTANCE.orderListToStoreOrderDTOList(orders);

    }

    public List<SellerOrderDTO> getOrderItemFromOrderId(UserDetails userDetails, String orderId) {
        var user = userRepository.getReferenceById(((User) userDetails).getId());
        var storeEmployee = storeEmployeeRepository.findByUser(user);

        if (storeEmployee.isEmpty()) {
            throw new UserNotFoundException("User is not connected with any store");
        }
        var order = orderRepository.findById(UUID.fromString(orderId));
        var orderItems = orderItemRepository.findAllByOrderAndProduct_Store(order.get(), storeEmployee.get().getStore());
        if (order.isEmpty() || orderItems.isEmpty()) {
            throw new UnAuthorizedEmployeeException("Unauthorized Request");
        }
        return OrderItemMapper.INSTANCE.orderItemListToSellerOrderDTOList(orderItems);
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
        checkOrderCompleted(orderItem.get());

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

    private void checkOrderCompleted(OrderItem orderItem) {
        var orderItems=orderItemRepository.findAllByOrderAndProduct_Store(orderItem.getOrder(),orderItem.getStore());

        for(OrderItem item:orderItems){
            if(item.getStatusType().equals(StatusType.ACCEPTED) ||
                    item.getStatusType().equals(StatusType.PENDING) ||
                    item.getStatusType().equals(StatusType.DISPATCHED)
            ){
                return;
            }
        }

        List<OrderItemHistory> orderItemHistories=new ArrayList<>();
        OrderHistory orderHistory= OrderHistory.builder()
                .id(orderItem.getOrder().getId())
                .buyer(orderItem.getOrder().getBuyer())
                .orderDate(orderItem.getOrder().getCreated_t())
                .orderItemHistories(orderItemHistories)
                .build();

        for(OrderItem item:orderItems) {
            var orderItemHistory = OrderItemHistory.builder()
                    .id(item.getId())
                    .productId(item.getProduct().getId())
                    .quantity(item.getQuantity())
                    .orderHistory(orderHistory)
                    .store(item.getStore())
                    .statusType(item.getStatusType())
                    .productPrice(item.getProductPrice())
                    .build();
            orderItemHistories.add(orderItemHistory);

        }

        orderHistoryRepository.save(orderHistory);

        orderItemRepository.deleteAll(orderItems);
        orderRepository.delete(orderItem.getOrder());
    }

    public List<StoreOrderDTO> getStoreOrderHistory(UserDetails userDetails) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());
        var storeEmployee= storeEmployeeRepository.findByUser(user);

        if(storeEmployee.isEmpty()){
            throw new UserNotFoundException("User is not connected with any store");
        }

        var ordersHistory=orderHistoryRepository.findAllByStoreId(storeEmployee.get().getStore().getId());
        return StoreMapper.INSTANCE.orderHistoryListToStoreOrderDTOList(ordersHistory);
    }
}
