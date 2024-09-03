package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);

//    @Query(nativeQuery = true,
//            value =
//                    "SELECT * " +
//                            "FROM _user u " +
//                            "WHERE role = 'SELLER' " +
//                            "AND EXISTS (" +
//                            "SELECT 1 " +
//                            "FROM product p " +
//                            "WHERE p.owner_id = u.id AND p.visible = TRUE AND p.available_quantity>0)")
//    List<User> findAvailableStores();
}
