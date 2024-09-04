package com.OrderManagementSystem.Mappers;

import com.OrderManagementSystem.Entities.Order;
import com.OrderManagementSystem.Entities.OrderHistory;
import com.OrderManagementSystem.Entities.OrderItem;
import com.OrderManagementSystem.Entities.Store;
import com.OrderManagementSystem.Models.DTO.OrderItemDTO;
import com.OrderManagementSystem.Models.DTO.SellerDTO;
import com.OrderManagementSystem.Models.DTO.StoreOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StoreMapper {

    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.created_t", target = "orderDate")
    @Mapping(source = "order.buyer.email", target = "customerEmail")

    StoreOrderDTO orderToStoreOrderDTO(Order order);

    List<StoreOrderDTO> orderListToStoreOrderDTOList(List<Order> orders);

    @Mapping(source = "orderHistory.id", target = "orderId")
    @Mapping(source = "orderHistory.orderDate", target = "orderDate")
    @Mapping(source = "orderHistory.buyer.email", target = "customerEmail")
    StoreOrderDTO orderHistoryToStoreOrderDTO(OrderHistory orderHistory);

    List<StoreOrderDTO> orderHistoryListToStoreOrderDTOList(List<OrderHistory> orders);
}
