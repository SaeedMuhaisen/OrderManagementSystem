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

    @Mapping(source = "buyer.firstname", target = "firstName")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "created_t", target = "orderDate")
    @Mapping(source = "statusType", target = "status")
    @Mapping(source = "id", target = "orderId")
    List<SellerOrderDTO> orderListToSellerOrderDTOList(List<Order> orders);

    @Mapping(source = "buyer.firstname", target = "firstName")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "created_t", target = "orderDate")
    @Mapping(source = "statusType", target = "status")
    @Mapping(source = "id", target = "orderId")
    SellerOrderDTO orderToSellerOrderDTO(Order orders);



    @Mapping(source = "product.user.email", target = "sellerEmail")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "created_t", target = "orderDate")
    @Mapping(source = "statusType", target = "status")
    @Mapping(source = "id", target = "orderId")
    List<BuyerOrderDTO> orderListToBuyerOrderDTOList(List<Order> orders);

    @Mapping(source = "product.user.email", target = "sellerEmail")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "created_t", target = "orderDate")
    @Mapping(source = "statusType", target = "status")
    @Mapping(source = "id", target = "orderId")
    BuyerOrderDTO orderToBuyerOrderDTO(Order orders);
}