package com.OrderManagementSystem.CSR.Services;

import com.OrderManagementSystem.CSR.Controllers.StoreController;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServices {
    private final StoreRepository storeRepository;
    private final StoreEmployeeRepository storeEmployeeRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final SimpMessagingTemplate template;
    private final OrderRepository orderRepository;
    private final OrderStoreRepository orderStoreRepository;
    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);
    private final OrderItemHistoryRepository orderItemHistoryRepository;
    private final OrderHistoryRepository orderHistoryRepository;


    public List<SellerDTO> getAllAvailableStores() {
        var stores=storeRepository.findAll();
        return SellerMapper.INSTANCE.sellerListToSellerDTOList(stores.stream().filter(store -> store.getProducts().size()>0).toList());
    }

    //todo this is actually bad impl, a seller account can call a buyer endpoint to get competitor products i believe
    //productDTO should change to something else.
    //for buyer
    public List<ProductDTO> getStoreProducts(String sellersId) {
        var store   = storeRepository.getReferenceById(UUID.fromString(sellersId));
        return ProductMapper.INSTANCE.productListToProductDTOList(store.getProducts().stream().filter(product -> product.isVisible()).toList());
    }

    //for storeEmployee
    public List<ProductDTO> getStoreProductsByEmployee(UserDetails userDetails) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());
        var storeEmployee= storeEmployeeRepository.findByUser(user);

        if(storeEmployee.isEmpty()){
            throw new UserNotFoundException("User is not connected with any store");
        }
        return ProductMapper.INSTANCE.productListToProductDTOList(storeEmployee.get().getStore().getProducts());
    }

    //todo: refactor user
    public List<StoreOrderDTO> getActiveStoreOrders(UserDetails userDetails) {
        var user= userRepository.findById(((User) userDetails).getId());
        if(!user.isPresent()){
            throw new UserNotFoundException("Couldn't find the user");
        }
        var storeEmployee= storeEmployeeRepository.findByUser(user.get());

        if(storeEmployee.isEmpty()){
            throw new UserNotFoundException("User is not connected with any store");
        }
        var orderStores=orderStoreRepository.findAllByStore(storeEmployee.get().getStore());
        var filteredFinishedOrders= orderStores.stream().filter(item -> !item.isFinished()).map(OrderStore::getOrder).toList();
        return StoreMapper.INSTANCE.orderListToStoreOrderDTOList(filteredFinishedOrders);
    }

    public List<SellerOrderDTO> getOrderItemsByOrderId(UserDetails userDetails, String orderId) {
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
    @Transactional
    public void updateOrderItemStatus(UserDetails userDetails, UpdateOrderItemStatusDTO updateOrderItemStatusDTO) throws Exception {
        User user = userRepository.getReferenceById(((User) userDetails).getId());
        StoreEmployee storeEmployee = storeEmployeeRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("User is not connected with any store"));

        OrderItem orderItem = orderItemRepository.findById(UUID.fromString(updateOrderItemStatusDTO.getOrderItemId()))
                .orElseThrow(() -> new AccessDeniedException("Order item doesn't exist or doesn't belong to employee's store"));

        if (!orderItem.getProduct().getStore().getId().equals(storeEmployee.getStore().getId())) {
            throw new AccessDeniedException("Order item doesn't belong to employee's store");
        }

        StatusType oldStatus = orderItem.getStatusType();
        StatusType newStatus = StatusType.valueOf(updateOrderItemStatusDTO.getStatus());

        if (StatusType.ACCEPTED.isIllegalStatusTransition(oldStatus, newStatus)) {
            throw new OrderStatusIllegalTransitionException("Invalid status transition. oldStatus: " + oldStatus + ", newStatus: " + newStatus);
        }

        orderItem.setStatusType(newStatus);
        orderItemRepository.saveAndFlush(orderItem);

        UUID buyerId = orderItem.getOrder().getBuyer().getId();

        sendNotification(buyerId, orderItem, newStatus);

        checkStoreOrderItemsCompleted(orderItem.getOrder(), orderItem.getProduct().getStore());

    }

    private void checkStoreOrderItemsCompleted(Order order, Store store) throws Exception {
        List<OrderItem> orderItemsFromStore = order.getOrderItems().stream()
                .filter(item -> item.getProduct().getStore().equals(store))
                .collect(Collectors.toList());

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

    private void createOrderHistory(Order order) {
        OrderHistory orderHistory = OrderHistory.builder()
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


    private void deleteOrder(Order order) throws Exception {

        for(var orderItem:order.getOrderItems()){
            orderItem.setOrder(null);

        }

        for(var orderStore:order.getOrderStores()){
            orderStore.setOrder(null);
            orderStore.getStore().getOrderStores().remove(orderStore);
            orderStore.setStore(null);
            orderStoreRepository.delete(orderStore);
        }

        orderRepository.delete(order);
    }
    private void sendNotification(UUID buyerId, OrderItem orderItem, StatusType newStatus) throws JsonProcessingException {

        var message= UpdateStatusNotification.builder()
                .orderId(String.valueOf(orderItem.getOrder().getId()))
                .productId(String.valueOf(orderItem.getProduct().getId()))
                .newStatus(newStatus.name())
                .build();
        ObjectMapper objectMapper=new ObjectMapper();
        var strMessage= objectMapper.writeValueAsString(message);
        template.convertAndSend(
                "/topic/notification/" + buyerId,
                NotificationMessage.builder()
                        .notificationType(NotificationType.BUYER_UPDATE_ORDER_STATUS)
                        .message(strMessage)
                        .build()
        );
    }


    //todo: refactor user
    public List<StoreOrderDTO> getStoreOrderHistory(UserDetails userDetails) {
        var user= userRepository.findById(((User) userDetails).getId());
        if(!user.isPresent()){
            throw new UserNotFoundException("Couldn't find the user");
        }
        var storeEmployee= storeEmployeeRepository.findByUser(user.get());

        if(storeEmployee.isEmpty()){
            throw new UserNotFoundException("User is not connected with any store");
        }
        var orderHistory=orderHistoryRepository.findAllByStores(storeEmployee.get().getStore());
        var totalOrders=StoreMapper.INSTANCE.orderHistoryListToStoreOrderDTOList(orderHistory);

        var orderStores=orderStoreRepository.findAllByStore(storeEmployee.get().getStore());
        var filteredFinishedOrders=orderStores.stream().filter(OrderStore::isFinished).map(OrderStore::getOrder).toList();
        var finishedOrders= StoreMapper.INSTANCE.orderListToStoreOrderDTOList(filteredFinishedOrders);

        totalOrders.addAll(finishedOrders);

        return totalOrders;
    }

    public List<SellerOrderDTO> getOrderItemHistory(UserDetails userDetails, String orderHistoryId) {
        var user = userRepository.getReferenceById(((User) userDetails).getId());
        var storeEmployee = storeEmployeeRepository.findByUser(user);

        if (storeEmployee.isEmpty()) {
            throw new UserNotFoundException("User is not connected with any store");
        }
        var orderHistory = orderHistoryRepository.findById(UUID.fromString(orderHistoryId));
        if(orderHistory.isPresent()  &&
                orderHistory.get()
                        .getOrderItemHistories()
                        .stream().filter(
                                item->item.getStore().equals(storeEmployee.get().getStore())
                        )
                        .toList()
                        .isEmpty()
        ){
            throw new UnAuthorizedEmployeeException("User Not authorized for this operation");
        }
        if(orderHistory.isEmpty()){
            var unfinishedOrder= orderRepository.findById(UUID.fromString(orderHistoryId));
            if(unfinishedOrder.isEmpty() || unfinishedOrder
                    .get()
                    .getOrderStores()
                    .stream()
                    .filter(item->item.getStore()==storeEmployee.get().getStore())
                    .toList().isEmpty()){
                //means the user is trying to fetch something randomly from other store or the id is incorrect
                throw new UnAuthorizedEmployeeException("User Not authorized for this operation");

            }

            return OrderItemMapper.INSTANCE.orderItemListToSellerOrderDTOList(
                    unfinishedOrder.get().getOrderItems()
                            .stream()
                            .filter(
                                    item->item.getProduct()
                                            .getStore()
                                            .equals(storeEmployee.get().getStore()))
                            .toList());
        }
        else{

            var filteredList= orderHistory.get().getOrderItemHistories().stream().filter(item->item.getStore().equals(storeEmployee.get().getStore())).toList();
            return OrderItemMapper.INSTANCE.orderItemHistoryListToSellerOrderDTOList(filteredList);
        }
    }
}
