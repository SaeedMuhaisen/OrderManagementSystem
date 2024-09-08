package com.OrderManagementSystem.CSR.Services;

import com.OrderManagementSystem.CSR.Repositories.OrderHistoryRepository;
import com.OrderManagementSystem.CSR.Repositories.OrderRepository;
import com.OrderManagementSystem.CSR.Repositories.OrderStoreRepository;
import com.OrderManagementSystem.CSR.Repositories.StoreEmployeeRepository;
import com.OrderManagementSystem.Entities.OrderStore;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Entities.enums.EmployeeRole;
import com.OrderManagementSystem.Exceptions.StoreExceptions.UnAuthorizedEmployeeException;
import com.OrderManagementSystem.Mappers.OrderItemMapper;
import com.OrderManagementSystem.Mappers.StoreMapper;
import com.OrderManagementSystem.Models.DTO.SellerOrderDTO;
import com.OrderManagementSystem.Models.DTO.StoreOrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class OrderItemHistoryServices {
    private final OrderHistoryRepository orderHistoryRepository;
    private final StoreEmployeeRepository storeEmployeeRepository;
    private final OrderRepository orderRepository;


    public List<SellerOrderDTO> getOrderItemHistoryByOrderHistoryId(UserDetails userDetails, String orderHistoryId) {
        var storeEmployee = storeEmployeeRepository.findByUser(((User) userDetails));
        if(storeEmployee.isEmpty() || !storeEmployee.get().getEmployeeRole().equals(EmployeeRole.ADMIN)){
            throw new UnAuthorizedEmployeeException("Store Employee has no Authorization to do this operation");
        }
        var orderHistory = orderHistoryRepository.findById(UUID.fromString(orderHistoryId));
        if(orderHistory.isPresent()  &&
                orderHistory.get()
                        .getOrderItemHistories()
                        .stream().filter(
                                item->item.getStore().equals(storeEmployee.get().getStore())
                        )
                        .toList()
                        .isEmpty()
        ){
            throw new UnAuthorizedEmployeeException("User Not authorized for this operation");
        }
        if(orderHistory.isEmpty()){
            var unfinishedOrder= orderRepository.findById(UUID.fromString(orderHistoryId));
            if(unfinishedOrder.isEmpty() || unfinishedOrder
                    .get()
                    .getOrderStores()
                    .stream()
                    .filter(item->item.getStore()==storeEmployee.get().getStore())
                    .toList().isEmpty()){
                //means the user is trying to fetch something randomly from other store or the id is incorrect
                throw new UnAuthorizedEmployeeException("User Not authorized for this operation");

            }

            return OrderItemMapper.INSTANCE.orderItemListToSellerOrderDTOList(
                    unfinishedOrder.get().getOrderItems()
                            .stream()
                            .filter(
                                    item->item.getProduct()
                                            .getStore()
                                            .equals(storeEmployee.get().getStore()))
                            .toList());
        }
        else{

            var filteredList= orderHistory.get().getOrderItemHistories().stream().filter(item->item.getStore().equals(storeEmployee.get().getStore())).toList();
            return OrderItemMapper.INSTANCE.orderItemHistoryListToSellerOrderDTOList(filteredList);
        }
    }

}
