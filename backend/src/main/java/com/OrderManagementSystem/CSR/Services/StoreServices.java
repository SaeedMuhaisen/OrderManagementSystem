package com.OrderManagementSystem.CSR.Services;

import Mappers.SellerMapper;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Models.DTO.ProductDTO;
import com.OrderManagementSystem.Models.DTO.SellerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServices {
    private final UserRepository userRepository;

    public List<SellerDTO> getAllAvailableStores() {
        var sellers=userRepository.findAvailableStores();
        return SellerMapper.INSTANCE.sellerListToSellerDTOList(sellers);
    }


}
