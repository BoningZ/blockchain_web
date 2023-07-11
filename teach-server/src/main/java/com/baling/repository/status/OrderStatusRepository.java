package com.baling.repository.status;

import com.baling.models.status.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    List<OrderStatus> getAllBy();
}
