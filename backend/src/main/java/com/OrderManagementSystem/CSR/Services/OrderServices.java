package com.OrderManagementSystem.CSR.Services;

import Mappers.OrderMapper;
import com.OrderManagementSystem.CSR.Repositories.OrderRepository;
import com.OrderManagementSystem.CSR.Repositories.ProductRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Entities.Order;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Entities.enums.StatusType;
import com.OrderManagementSystem.Exceptions.OrderExceptions.OrderStatusIllegalTransitionException;
import com.OrderManagementSystem.Exceptions.OrderExceptions.ProductQuantityNotEnoughException;
import com.OrderManagementSystem.Models.DTO.BuyerOrderDTO;
import com.OrderManagementSystem.Models.DTO.CreateOrderDTO;
import com.OrderManagementSystem.Models.DTO.SellerOrderDTO;
import com.OrderManagementSystem.Models.DTO.UpdateOrderStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServices {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    public void createOrder(UserDetails userDetails, CreateOrderDTO createOrderDTO) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());
        var product= productRepository.getReferenceById(UUID.fromString(createOrderDTO.getProductId()));

        if(product.getAvailableQuantity()< createOrderDTO.getQuantity()){
            throw new ProductQuantityNotEnoughException("Product's available quantity is not enough to accept order");
        }

        var order =
                Order.builder()
                        .buyer(user)
                        .created_t(Instant.now())
                        .statusType(StatusType.PENDING)
                        .product(product).build();

        product.setAmountSold(product.getAmountSold()+ createOrderDTO.getQuantity());
        product.setAvailableQuantity(product.getAvailableQuantity()- createOrderDTO.getQuantity());
        //todo send notification for owner and perhaps also seller to confirm order placed?

        orderRepository.save(order);
        productRepository.save(product);

    }

    public List<SellerOrderDTO> getAllSellerOrders(UserDetails userDetails) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());
        var orders=orderRepository.findAllByProduct_User_Id(user.getId());

        return OrderMapper.INSTANCE.orderListToSellerOrderDTOList(orders);

    }

    public List<BuyerOrderDTO> getAllBuyerOrders(UserDetails userDetails) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());
        return OrderMapper.INSTANCE.orderListToBuyerOrderDTOList(user.getOrders());

    }

    public void updateOrderStatus(UserDetails userDetails, UpdateOrderStatusDTO updateOrderStatusDTO) throws AccessDeniedException {
        var user = userRepository.getReferenceById(((User) userDetails).getId());
        var order = orderRepository.getReferenceById(UUID.fromString(updateOrderStatusDTO.getOrderId()));

        if (!order.getProduct().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Buyer cannot update the status of an order with a product that doesn't belong to them.");
        }

        var oldStatus = order.getStatusType();
        var newStatus = StatusType.valueOf(updateOrderStatusDTO.getStatus());


        if (StatusType.ACCEPTED.isIllegalStatusTransition(oldStatus,newStatus)) {
            throw new OrderStatusIllegalTransitionException("Invalid status transition. oldStatus: "+oldStatus+", newStatus: "+newStatus);
        }

        order.setStatusType(newStatus);
        orderRepository.save(order);

        //todo send notification to buyer

    }
}
