package com.OrderManagementSystem.Mappers;

import com.OrderManagementSystem.Entities.Store;
import com.OrderManagementSystem.Models.DTO.SellerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SellerMapper {

    SellerMapper INSTANCE = Mappers.getMapper(SellerMapper.class);


    @Mapping(source = "id", target = "sellerId")
    @Mapping(source = "name",target="sellerName")
    List<SellerDTO> sellerListToSellerDTOList(List<Store> stores);

    @Mapping(source = "id", target = "sellerId")
    @Mapping(source = "name",target="sellerName")
    SellerDTO sellerToSellerDTO(Store store);

}