package com.OrderManagementSystem.CSR.Repositories;

import com.OrderManagementSystem.Entities.NotificationQueue;
import com.OrderManagementSystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationQueueRepository extends JpaRepository<NotificationQueue,Long> {

    List<NotificationQueue> findAllByUser(User user);

}
