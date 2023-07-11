package com.baling.repository.status;

import com.baling.models.status.LogisticsStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogisticsStatusRepository extends JpaRepository<LogisticsStatus,Integer> {
    List<LogisticsStatus> getAllBy();
}
