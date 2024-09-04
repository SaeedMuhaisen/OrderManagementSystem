package com.OrderManagementSystem.CSR.Services;

import com.OrderManagementSystem.Mappers.OrderItemMapper;
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
import com.OrderManagementSystem.Models.DTO.*;
import com.OrderManagementSystem.Models.Notifications.NotificationMessage;
import com.OrderManagementSystem.Models.Notifications.NotificationType;
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


    @Transactional
    public void createOrder(UserDetails userDetails, CreateOrderDTO createOrderDTO) {
        var user= userRepository.findById(((User) userDetails).getId());
        if(!user.isPresent()){
            throw new UserNotFoundException("User couldn't be found");
        }
        List<Product> orderProducts=new ArrayList<>();
        List<OrderItem> orderItems=new ArrayList<>();

        var order = Order.builder()
                .buyer(user.get())
                .created_t(Instant.now())
                .orderItems(orderItems)
                .build();

        var savedOrder=orderRepository.save(order);

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
                    .order(savedOrder)
                    .store(product.get().getStore())
                    .build();
            orderItems.add(orderItem);
        }

        productRepository.saveAll(orderProducts);
        orderItemRepository.saveAll(orderItems);

        Set<Store> stores=new HashSet<>();
        Set<StoreEmployee> admins = new HashSet<>();
        for(var product: orderProducts){
            stores.add(product.getStore());
        }
        for(var store:stores) {
          for (var adminOfStore : store.getEmployees().stream().filter(storeEmployee -> storeEmployee.getEmployeeRole().equals(EmployeeRole.ADMIN)).toList()) {
              admins.add(adminOfStore);
          }
        }
        for (var admin : admins) {
            var newOrderItems = orderItems.stream().filter(orderItem -> {
                return orderItem.getProduct().getStore().getId() == admin.getStore().getId();
            }).toList();
            template.convertAndSend("/topic/notification/" + admin.getUser().getId(),
                    NotificationMessage.builder()
                            .notificationType(NotificationType.SELLER_NEW_ORDER)
                            .message(OrderItemMapper.INSTANCE.orderItemListToSellerOrderDTOList(newOrderItems))
                            .build()
            );
        }
    }

    public List<BuyerOrderDTO> getAllBuyerOrders(UserDetails userDetails) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());
        return OrderMapper.INSTANCE.orderListToBuyerOrderDTOList(user.getOrders());
    }

}
