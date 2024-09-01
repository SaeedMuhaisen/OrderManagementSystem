package com.OrderManagementSystem.CSR.Services;

import Mappers.OrderItemMapper;
import Mappers.OrderMapper;
import com.OrderManagementSystem.CSR.Repositories.OrderItemRepository;
import com.OrderManagementSystem.CSR.Repositories.OrderRepository;
import com.OrderManagementSystem.CSR.Repositories.ProductRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Entities.Order;
import com.OrderManagementSystem.Entities.OrderItem;
import com.OrderManagementSystem.Entities.Product;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Entities.enums.StatusType;
import com.OrderManagementSystem.Exceptions.AuthExceptions.UserNotFoundException;
import com.OrderManagementSystem.Exceptions.OrderExceptions.OrderContainsIllegalProduct;
import com.OrderManagementSystem.Exceptions.OrderExceptions.OrderStatusIllegalTransitionException;
import com.OrderManagementSystem.Exceptions.OrderExceptions.ProductQuantityNotEnoughException;
import com.OrderManagementSystem.Models.DTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        List<Product> products=new ArrayList<>();
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

            products.add(product.get());
            product.get().setAvailableQuantity(product.get().getAvailableQuantity()-orderItemDTO.getQuantity());
            product.get().setAmountSold(product.get().getAmountSold()+orderItemDTO.getQuantity());

            var newAvailableQuantity= product.get().getAvailableQuantity()-orderItemDTO.getQuantity();
            if(newAvailableQuantity==0){
                product.get().setVisible(false);
            }

            var orderItem = OrderItem.builder()
                    .quantity(orderItemDTO.getQuantity())
                    .product(product.get())
                    .statusType(StatusType.PENDING)
                    .order(savedOrder)
                    .build();
            orderItems.add(orderItem);
        }



        orderItemRepository.saveAll(orderItems);

        productRepository.saveAll(products);
        for(var product: products){
            template.convertAndSend("/topic/notification/"+product.getUser().getId(), "New Order Received!");
        }
    }

    public List<SellerOrderDTO> getAllSellerOrders(UserDetails userDetails) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());
        var orderItems=orderItemRepository.findAllByProduct_User_Id(user.getId());

        return OrderItemMapper.INSTANCE.orderItemListToSellerOrderDTOList(orderItems);

    }

    public List<BuyerOrderDTO> getAllBuyerOrders(UserDetails userDetails) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());
        return OrderMapper.INSTANCE.orderListToBuyerOrderDTOList(user.getOrders());

    }

    public void updateOrderItemStatus(UserDetails userDetails, UpdateOrderItemStatusDTO updateOrderItemStatusDTO) throws AccessDeniedException {
        var user = userRepository.findById(((User) userDetails).getId());
        var orderItem = orderItemRepository.findById(UUID.fromString(updateOrderItemStatusDTO.getOrderItemId()));
        if (!orderItem.isPresent() || !orderItem.get().getProduct().getUser().getId().equals(user.get().getId()) || orderItem.get().getOrder().getBuyer().getId()==null) {
            throw new AccessDeniedException("Buyer cannot update the status of orderItem that doesn't belong to them.");
        }

        var oldStatus = orderItem.get().getStatusType();
        var newStatus = StatusType.valueOf(updateOrderItemStatusDTO.getStatus());


        if (StatusType.ACCEPTED.isIllegalStatusTransition(oldStatus,newStatus)) {
            throw new OrderStatusIllegalTransitionException("Invalid status transition. oldStatus: "+oldStatus+", newStatus: "+newStatus);
        }

        orderItem.get().setStatusType(newStatus);
        orderItemRepository.save(orderItem.get());
        var buyer=orderRepository.getReferenceById(orderItem.get().getOrder().getId()).getBuyer();
        template.convertAndSend("/topic/notification/"+buyer.getId(), "Order Status updated!");

    }
}
