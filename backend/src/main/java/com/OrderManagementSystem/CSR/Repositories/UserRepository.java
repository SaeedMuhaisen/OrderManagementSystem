package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Optional<User> findByOauthId(String oauthId);
    Optional<User> findById(UUID id);
}
