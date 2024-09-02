package Mappers;


import com.OrderManagementSystem.Entities.Order;
import com.OrderManagementSystem.Entities.OrderItem;
import com.OrderManagementSystem.Models.DTO.BuyerOrderDTO;
import com.OrderManagementSystem.Models.DTO.OrderItemDTO;
import com.OrderManagementSystem.Models.DTO.SellerOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);


    @Mapping(source = "created_t", target = "orderDate")
    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "orderItems" ,target="orderItems")
    List<BuyerOrderDTO> orderListToBuyerOrderDTOList(List<Order> orders);

    @Mapping(source = "created_t", target = "orderDate")
    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "orderItems" ,target="orderItems")
    BuyerOrderDTO orderToBuyerOrderDTO(Order orders);

    @Mapping(source = "quantity",target="quantity" )
    @Mapping(source = "product.id",target="productId" )
    @Mapping(source="productPrice", target = "productPrice")
    @Mapping(source = "statusType",target = "status")
    OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem);
}