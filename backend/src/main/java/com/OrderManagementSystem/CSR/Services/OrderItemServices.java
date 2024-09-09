package com.OrderManagementSystem.CSR.Services;

import com.OrderManagementSystem.CSR.Repositories.*;
import com.OrderManagementSystem.Entities.*;
import com.OrderManagementSystem.Entities.enums.EmployeeRole;
import com.OrderManagementSystem.Entities.enums.StatusType;
import com.OrderManagementSystem.Exceptions.OrderExceptions.OrderStatusIllegalTransitionException;
import com.OrderManagementSystem.Exceptions.StoreExceptions.UnAuthorizedEmployeeException;
import com.OrderManagementSystem.Mappers.OrderItemMapper;
import com.OrderManagementSystem.Models.DTO.SellerOrderDTO;
import com.OrderManagementSystem.Models.DTO.UpdateOrderItemStatusDTO;
import com.OrderManagementSystem.Models.Notifications.NotificationMessage;
import com.OrderManagementSystem.Models.Notifications.NotificationType;
import com.OrderManagementSystem.Models.Notifications.UpdateStatusNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class OrderItemServices {
    private final OrderRepository orderRepository;
    private final StoreEmployeeRepository storeEmployeeRepository;
    private final OrderStoreRepository orderStoreRepository;
    private final OrderItemRepository orderItemRepository;
    private final StoreRepository storeRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final MessageBrokerServices messageBrokerServices;
    private final ProductRepository productRepository;

    @Transactional
    public void updateOrderItemStatus(UserDetails userDetails, UpdateOrderItemStatusDTO updateOrderItemStatusDTO) throws Exception {
        var storeEmployee = storeEmployeeRepository.findByUser(((User) userDetails));
        if(storeEmployee.isEmpty() || !storeEmployee.get().getEmployeeRole().equals(EmployeeRole.ADMIN)){
            throw new UnAuthorizedEmployeeException("Store Employee has no Authorization to do this operation");
        }

        var orderItemCheck = orderItemRepository.findById(UUID.fromString(updateOrderItemStatusDTO.getOrderItemId()));
        if(orderItemCheck.isEmpty() || !orderItemCheck.get().getProduct().getStore().getId().equals(storeEmployee.get().getStore().getId())){
            throw new UnAuthorizedEmployeeException("Store Employee has no Authorization to do this operation");
        }
        var orderItem=orderItemCheck.get();

        StatusType oldStatus = orderItem.getStatusType();
        StatusType newStatus = StatusType.valueOf(updateOrderItemStatusDTO.getStatus());

        if (StatusType.ACCEPTED.isIllegalStatusTransition(oldStatus, newStatus)) {
            throw new OrderStatusIllegalTransitionException("Invalid status transition. oldStatus: " + oldStatus + ", newStatus: " + newStatus);
        }
        orderItem.setStatusType(newStatus);
        if(newStatus.equals(StatusType.CANCELED_BY_SELLER)){
            var product=orderItem.getProduct();
            product.setAmountSold(product.getAmountSold()-orderItem.getQuantity());
            product.setAvailableQuantity(product.getAvailableQuantity()+orderItem.getQuantity());
            product.setAmountReturned(product.getAmountReturned()+orderItem.getQuantity());
            productRepository.save(product);
        }
        orderItemRepository.saveAndFlush(orderItem);
        sendNotification(orderItem.getOrder().getBuyer(),orderItem, newStatus);
        checkStoreOrderItemsCompleted(orderItem.getOrder(), orderItem.getProduct().getStore());
    }

    private void checkStoreOrderItemsCompleted(Order orderEntity, Store storeEntity) throws Exception {
        var orderCheck= orderRepository.findById(orderEntity.getId());
        var storeCheck= storeRepository.findById(storeEntity.getId());
        if(orderCheck.isEmpty() || storeCheck.isEmpty()){
            throw new Exception("checkStoreOrderItemsCompleted() - Couldn't find store or order to update");
        }
        var order=orderCheck.get();
        var store=storeCheck.get();
        List<OrderItem> orderItemsFromStore = order.getOrderItems().stream()
                .filter(item -> item.getProduct().getStore().getId().equals(store.getId()))
                .toList();

        boolean allCompleted = orderItemsFromStore.stream()
                .allMatch(item -> !item.getStatusType().equals(StatusType.ACCEPTED) &&
                        !item.getStatusType().equals(StatusType.PENDING) &&
                        !item.getStatusType().equals(StatusType.DISPATCHED));

        if (allCompleted) {
            OrderStore orderStore = orderStoreRepository.findByOrderAndStore(order, store)
                    .orElseThrow(() -> new RuntimeException("Couldn't find order_store for an active order"));
            orderStore.setFinished(true);
            orderStoreRepository.save(orderStore);

            checkFullOrderCompleted(order);
        }
    }

    private void checkFullOrderCompleted(Order order) throws Exception {
        boolean allStoresFinished = orderStoreRepository.findAllByOrder(order).stream()
                .allMatch(OrderStore::isFinished);

        if (allStoresFinished) {
            createOrderHistory(order);
            deleteOrder(order);
        }
    }

    private void createOrderHistory(Order orderEntity) throws Exception {
        var orderCheck=orderRepository.findById(orderEntity.getId());
        if(orderCheck.isEmpty()) {
            throw new Exception("createOrderHistory() - Couldn't find order history!");
        }

        var order=orderCheck.get();

        var orderHistory = OrderHistory.builder()
                .orderItemHistories(new ArrayList<>())
                .stores(order.getOrderStores().stream().map(OrderStore::getStore).collect(Collectors.toSet()))
                .created_t(order.getCreated_t())
                .buyer(order.getBuyer())
                .build();

        for (OrderItem orderItem : order.getOrderItems()) {
            OrderItemHistory orderItemHistory = OrderItemHistory.builder()
                    .orderHistory(orderHistory)
                    .statusType(orderItem.getStatusType())
                    .quantity(orderItem.getQuantity())
                    .productId(orderItem.getProduct().getId())
                    .store(orderItem.getProduct().getStore())
                    .productPrice(orderItem.getProductPrice())
                    .build();
            orderHistory.getOrderItemHistories().add(orderItemHistory);
        }

        orderHistoryRepository.save(orderHistory);
    }


    private void deleteOrder(Order orderEntity) throws Exception {
        var orderCheck=orderRepository.findById(orderEntity.getId());
        if(orderCheck.isEmpty()){
            throw new Exception("deleteOrder() - Couldn't find order");
        }
        var order=orderCheck.get();

        for(var orderItem:order.getOrderItems()){
            orderItem.setOrder(null);
            orderItemRepository.delete(orderItem);
        }
        for(var orderStore:order.getOrderStores()){
            orderStore.setOrder(null);
            orderStore.getStore().getOrderStores().remove(orderStore);
            orderStore.setStore(null);
            orderStoreRepository.delete(orderStore);
        }

        orderRepository.delete(order);
    }

    private void sendNotification(User buyer, OrderItem orderItem, StatusType newStatus) throws JsonProcessingException {

        var message= UpdateStatusNotification.builder()
                .orderId(String.valueOf(orderItem.getOrder().getId()))
                .productId(String.valueOf(orderItem.getProduct().getId()))
                .newStatus(newStatus.name())
                .build();
        ObjectMapper objectMapper=new ObjectMapper();

        var strMessage= objectMapper.writeValueAsString(message);

        messageBrokerServices.sendNotification("/topic/notification/" + buyer.getId(),buyer,
                NotificationMessage.builder()
                        .notificationType(NotificationType.BUYER_UPDATE_ORDER_STATUS)
                        .message(strMessage)
                        .build());
    }

    public List<SellerOrderDTO> getOrderItemsByOrderId(UserDetails userDetails, String orderId) {

        var storeEmployee = storeEmployeeRepository.findByUser(((User) userDetails));
        if(storeEmployee.isEmpty() || !storeEmployee.get().getEmployeeRole().equals(EmployeeRole.ADMIN)){
            throw new UnAuthorizedEmployeeException("Store Employee has no Authorization to do this operation");
        }

        var order = orderRepository.findById(UUID.fromString(orderId));
        if(order.isEmpty()){
            throw new UnAuthorizedEmployeeException("Store Employee has no Authorization to do this operation");
        }
        var orderItems= order.get()
                .getOrderItems()
                .stream()
                .filter(orderItem -> orderItem.getProduct().getStore().equals(storeEmployee.get().getStore()))
                .toList();

        return OrderItemMapper.INSTANCE.orderItemListToSellerOrderDTOList(orderItems);
    }
}
