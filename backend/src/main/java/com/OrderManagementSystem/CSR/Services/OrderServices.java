package com.OrderManagementSystem.CSR.Services;

import com.OrderManagementSystem.Mappers.OrderMapper;
import com.OrderManagementSystem.CSR.Repositories.OrderItemRepository;
import com.OrderManagementSystem.CSR.Repositories.OrderRepository;
import com.OrderManagementSystem.CSR.Repositories.ProductRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Entities.*;
import com.OrderManagementSystem.Entities.enums.EmployeeRole;
import com.OrderManagementSystem.Entities.enums.StatusType;
import com.OrderManagementSystem.Exceptions.AuthExceptions.UserNotFoundException;
import com.OrderManagementSystem.Exceptions.OrderExceptions.OrderContainsIllegalProduct;
import com.OrderManagementSystem.Exceptions.OrderExceptions.ProductQuantityNotEnoughException;
import com.OrderManagementSystem.Mappers.StoreMapper;
import com.OrderManagementSystem.Models.DTO.*;
import com.OrderManagementSystem.Models.Notifications.NotificationMessage;
import com.OrderManagementSystem.Models.Notifications.NotificationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServices {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final SimpMessagingTemplate template;
    private final NotificationServices notificationServices;
    private final MessageBrokerServices messageBrokerServices;

    @Transactional
    public void createOrder(UserDetails userDetails, CreateOrderDTO createOrderDTO) throws JsonProcessingException {
        var user= userRepository.findById(((User) userDetails).getId());
        if(!user.isPresent()){
            throw new UserNotFoundException("User couldn't be found");
        }
        List<Product> orderProducts=new ArrayList<>();
        List<OrderItem> orderItems=new ArrayList<>();

        Set<OrderStore> orderStores=new HashSet<>();

        var order = Order.builder()
                .buyer(user.get())
                .created_t(Instant.now())
                .orderStores(orderStores)
                .orderItems(orderItems)
                .build();

//        var savedOrder=orderRepository.save(order);

        for(OrderItemDTO orderItemDTO: createOrderDTO.getProducts()){
            var product= productRepository.findById(UUID.fromString(orderItemDTO.getProductId()));

            if(!product.isPresent() || !product.get().isVisible() ){
                throw new OrderContainsIllegalProduct("Product doesn't Exists");
            }
            if(product.get().getAvailableQuantity()< orderItemDTO.getQuantity()) {
                throw new ProductQuantityNotEnoughException("Product's available quantity is not enough to accept order");
            }

            orderProducts.add(product.get());
            product.get().setAvailableQuantity(product.get().getAvailableQuantity()-orderItemDTO.getQuantity());
            product.get().setAmountSold(product.get().getAmountSold()+orderItemDTO.getQuantity());
            var newAvailableQuantity= product.get().getAvailableQuantity()-orderItemDTO.getQuantity();
            if(newAvailableQuantity==0){
                product.get().setVisible(false);
            }

            var orderItem = OrderItem.builder()
                    .quantity(orderItemDTO.getQuantity())
                    .product(product.get())
                    .productPrice(product.get().getPrice())
                    .statusType(StatusType.PENDING)
                    .order(order)
                    .build();
            orderItems.add(orderItem);

            if(order.getOrderStores().stream().filter(item->item.getStore().getId().equals(product.get().getStore().getId())).toList().isEmpty()){
                var orderStore=OrderStore.builder()
                        .store(product.get().getStore())
                        .order(order)
                        .finished(false)
                        .build();

                orderStores.add(orderStore);
            }

        }

        productRepository.saveAll(orderProducts);
        orderRepository.save(order);
        //orderItemRepository.saveAll(orderItems);
        //O(n^2) nested todo:Get rid of the nested forloop..
        Set<StoreEmployee> admins=new HashSet<>();
        for(var orderStore:orderStores) {
          for (var adminOfStore : orderStore.getStore().getEmployees().stream().filter(storeEmployee -> storeEmployee.getEmployeeRole().equals(EmployeeRole.ADMIN)).toList()) {
              admins.add(adminOfStore);
          }
        }
        for (var admin : admins) {

            var message=StoreMapper.INSTANCE.orderToStoreOrderDTO(order);
            var objectMapper=new ObjectMapper();
            var messageStr=objectMapper.writeValueAsString(message);
            messageBrokerServices.sendNotification(
                    "/topic/notification/"+admin.getUser().getId()
                    ,admin.getUser()
                    ,NotificationMessage.builder()
                    .notificationType(NotificationType.SELLER_ORDER)
                    .message(messageStr)
                    .build());
        }
    }

    public List<BuyerOrderDTO> getCustomerActiveOrders(UserDetails userDetails) {
        var user= userRepository.findById(((User) userDetails).getId());
        return OrderMapper.INSTANCE.orderListToBuyerOrderDTOList(user.get().getOrders());
    }

    public List<BuyerOrderDTO> getCustomerOrdersHistory(UserDetails userDetails) {
        var user= userRepository.findById(((User) userDetails).getId());
        return OrderMapper.INSTANCE.orderHistoryListToBuyerOrderDTOList(user.get().getOrderHistory());
    }
}
