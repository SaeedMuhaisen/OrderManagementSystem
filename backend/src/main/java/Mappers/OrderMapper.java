package Mappers;


import com.OrderManagementSystem.Entities.Order;
import com.OrderManagementSystem.Models.DTO.BuyerOrderDTO;
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
    List<BuyerOrderDTO> orderListToBuyerOrderDTOList(List<Order> orders);

    @Mapping(source = "created_t", target = "orderDate")
    @Mapping(source = "id", target = "orderId")
    BuyerOrderDTO orderToBuyerOrderDTO(Order orders);
}