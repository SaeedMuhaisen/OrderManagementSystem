package com.OrderManagementSystem.Mappers;

import com.OrderManagementSystem.Entities.OrderItem;
import com.OrderManagementSystem.Entities.OrderItemHistory;
import com.OrderManagementSystem.Models.DTO.SellerOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(source = "order.buyer.firstname",target="firstName" )
    @Mapping(source = "product.id",target="productId" )
    @Mapping(source = "order.created_t",target="orderDate" )
    @Mapping(source = "statusType",target="status" )
    @Mapping(source = "id", target = "orderItemId")
    List<SellerOrderDTO> orderItemListToSellerOrderDTOList(List<OrderItem> orderItems);

    @Mapping(source = "order.buyer.firstname",target="firstName" )
    @Mapping(source = "product.id",target="productId" )
    @Mapping(source = "order.created_t",target="orderDate" )
    @Mapping(source = "statusType",target="status" )
    @Mapping(source = "id", target = "orderItemId")

    SellerOrderDTO orderItemToSellerOrderDTO(OrderItem orderItem);


    @Mapping(source = "orderHistory.buyer.firstname",target="firstName" )
    @Mapping(source = "productId",target="productId" )
    @Mapping(source = "orderHistory.created_t",target="orderDate" )
    @Mapping(source = "statusType",target="status" )
    @Mapping(source = "id", target = "orderItemId")
    List<SellerOrderDTO> orderItemHistoryListToSellerOrderDTOList(List<OrderItemHistory> orderItemHistories);

    @Mapping(source = "orderHistory.buyer.firstname",target="firstName" )
    @Mapping(source = "productId",target="productId" )
    @Mapping(source = "orderHistory.created_t",target="orderDate" )
    @Mapping(source = "statusType",target="status" )
    @Mapping(source = "id", target = "orderItemId")
    SellerOrderDTO orderItemHistoryToSellerOrderDTO(OrderItemHistory orderItemHistory);

}