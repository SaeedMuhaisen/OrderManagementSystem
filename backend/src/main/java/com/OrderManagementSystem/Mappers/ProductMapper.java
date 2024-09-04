package com.OrderManagementSystem.Mappers;

import com.OrderManagementSystem.Entities.Product;
import com.OrderManagementSystem.Models.DTO.ProductDTO;
import com.OrderManagementSystem.Models.DTO.StoreProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    ProductDTO productToProductDTO(Product product);
    List<ProductDTO> productListToProductDTOList(List<Product> products);

    List<StoreProductDTO> productListToStoreProductDTOList(List<Product> products);
}
