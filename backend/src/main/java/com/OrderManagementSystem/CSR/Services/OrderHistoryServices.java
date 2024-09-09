package com.OrderManagementSystem.CSR.Services;

import com.OrderManagementSystem.CSR.Repositories.*;
import com.OrderManagementSystem.Entities.OrderStore;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Entities.enums.EmployeeRole;
import com.OrderManagementSystem.Exceptions.AuthExceptions.UserNotFoundException;
import com.OrderManagementSystem.Exceptions.StoreExceptions.UnAuthorizedEmployeeException;
import com.OrderManagementSystem.Mappers.OrderMapper;
import com.OrderManagementSystem.Mappers.StoreMapper;
import com.OrderManagementSystem.Models.DTO.BuyerOrderDTO;
import com.OrderManagementSystem.Models.DTO.StoreOrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderHistoryServices {

    private final OrderHistoryRepository orderHistoryRepository;
    private final StoreEmployeeRepository storeEmployeeRepository;
    private final OrderStoreRepository orderStoreRepository;
    private final UserRepository userRepository;
    public List<StoreOrderDTO> getStoreOrderHistory(UserDetails userDetails) {
        var storeEmployee = storeEmployeeRepository.findByUser(((User) userDetails));
        if(storeEmployee.isEmpty() || !storeEmployee.get().getEmployeeRole().equals(EmployeeRole.ADMIN)){
            throw new UnAuthorizedEmployeeException("Store Employee has no Authorization to do this operation");
        }
        var historyOrders=orderHistoryRepository.findAllByStores(storeEmployee.get().getStore());

        var storeOrderDTOs = StoreMapper.INSTANCE.orderHistoryListToStoreOrderDTOList(historyOrders);

        //we add also active orders that are finished from this store side
        var orderStores = orderStoreRepository.findAllByStore(storeEmployee.get().getStore());
        var activeFinishedOrders = orderStores.stream().filter(OrderStore::isFinished).map(OrderStore::getOrder).toList();

        storeOrderDTOs.addAll(StoreMapper.INSTANCE.orderListToStoreOrderDTOList(activeFinishedOrders));

        return storeOrderDTOs;
    }
    public List<BuyerOrderDTO> getCustomerOrdersHistory(UserDetails userDetails) {
        var user= userRepository.findById(((User) userDetails).getId());
        if(user.isEmpty()){
            throw new UserNotFoundException("User couldn't be found");
        }
        return OrderMapper.INSTANCE.orderHistoryListToBuyerOrderDTOList(user.get().getOrderHistory());

    }
}
