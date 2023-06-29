package com.baling.repository.log;

import com.baling.models.log.Operate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperateRepository extends JpaRepository<Operate,Integer> {
}
