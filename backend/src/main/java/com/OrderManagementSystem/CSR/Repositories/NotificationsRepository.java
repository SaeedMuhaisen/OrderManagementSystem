package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.Notifications;
import com.OrderManagementSystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {

    List<Notifications> findAllByUser(User user);
    void deleteBySessionId(String sessionId);
    Optional<Notifications> findByUserAndSessionId(User user, String sessionId);

}
