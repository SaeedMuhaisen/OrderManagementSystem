package Mappers;


import com.OrderManagementSystem.Entities.Order;
import com.OrderManagementSystem.Models.DTO.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "user.firstname", target = "firstName")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "created_t", target = "orderDate")
    @Mapping(source = "statusType", target = "status")
    @Mapping(source = "id", target = "orderId")
    List<OrderDTO> orderListToOrderDTOList(List<Order> orders);

    @Mapping(source = "user.firstname", target = "firstName")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "created_t", target = "orderDate")
    @Mapping(source = "statusType", target = "status")
    @Mapping(source = "id", target = "orderId")
    OrderDTO orderToOrderDTO(Order orders);
}