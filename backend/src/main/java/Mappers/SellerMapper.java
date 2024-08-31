package Mappers;

import com.OrderManagementSystem.Entities.Order;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Models.DTO.SellerOrderDTO;
import com.OrderManagementSystem.Models.DTO.SellerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SellerMapper {

    SellerMapper INSTANCE = Mappers.getMapper(SellerMapper.class);


    @Mapping(source = "id", target = "sellerId")
    @Mapping(source = "firstname",target="sellerName")
    List<SellerDTO> sellerListToSellerDTOList(List<User> sellers);

    @Mapping(source = "id", target = "sellerId")
    @Mapping(source = "firstname",target="sellerName")
    SellerDTO sellerToSellerDTO(User seller);

}