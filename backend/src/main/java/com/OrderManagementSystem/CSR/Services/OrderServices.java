package com.OrderManagementSystem.CSR.Services;

import Mappers.OrderMapper;
import Mappers.ProductMapper;
import com.OrderManagementSystem.CSR.Repositories.OrderRepository;
import com.OrderManagementSystem.CSR.Repositories.ProductRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Entities.Order;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Entities.enums.StatusType;
import com.OrderManagementSystem.Exceptions.OrderExceptions.ProductQuantityNotEnoughException;
import com.OrderManagementSystem.Models.DTO.CreateOrderDTO;
import com.OrderManagementSystem.Models.DTO.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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
                        .user(user)
                        .created_t(Instant.now())
                        .statusType(StatusType.PENDING)
                        .product(product).build();

        product.setAmountSold(product.getAmountSold()+ createOrderDTO.getQuantity());
        product.setAvailableQuantity(product.getAvailableQuantity()- createOrderDTO.getQuantity());
        //todo send notification for owner and perhaps also seller to confirm order placed?

        orderRepository.save(order);
        productRepository.save(product);

    }

    public List<OrderDTO> getAllOrders(UserDetails userDetails) {
        var user= userRepository.getReferenceById(((User) userDetails).getId());
        var orders=orderRepository.findAllByProduct_User_Id(user.getId());

        return OrderMapper.INSTANCE.orderListToOrderDTOList(orders);

    }
}
