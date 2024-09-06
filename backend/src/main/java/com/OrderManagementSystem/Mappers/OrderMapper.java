package com.OrderManagementSystem.Mappers;


import com.OrderManagementSystem.Entities.Order;
import com.OrderManagementSystem.Entities.OrderHistory;
import com.OrderManagementSystem.Entities.OrderItem;
import com.OrderManagementSystem.Entities.OrderItemHistory;
import com.OrderManagementSystem.Models.DTO.BuyerOrderDTO;
import com.OrderManagementSystem.Models.DTO.OrderItemDTO;
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


    List<BuyerOrderDTO> orderHistoryListToBuyerOrderDTOList(List<OrderHistory> orders);
    @Mapping(source = "created_t", target = "orderDate")
    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "orderItems" ,target="orderItems")
    BuyerOrderDTO orderToBuyerOrderDTO(Order orders);

    @Mapping(source = "quantity",target="quantity" )
    @Mapping(source = "product.id",target="productId" )
    @Mapping(source="productPrice", target = "productPrice")
    @Mapping(source = "statusType",target = "status")
    OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem);

    @Mapping(source = "quantity",target="quantity" )
    @Mapping(source = "productId",target="productId" )
    @Mapping(source="productPrice", target = "productPrice")
    @Mapping(source = "statusType",target = "status")
    OrderItemDTO orderItemHistoryToOrderItemDTO(OrderItemHistory orderItemHistory);


    @Mapping(source = "created_t", target = "orderDate")
    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "orderItemHistories" ,target="orderItems")
    BuyerOrderDTO orderToBuyerOrderDTO(OrderHistory orderHistory);
}