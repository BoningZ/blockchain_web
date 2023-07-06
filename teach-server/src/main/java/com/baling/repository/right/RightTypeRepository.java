package com.baling.repository.right;

import com.baling.models.right.ERightType;
import com.baling.models.right.RightType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RightTypeRepository extends JpaRepository<RightType, Integer> {
    RightType getByValue(ERightType value);
    List<RightType> getAllBy();
    List<RightType> getRightTypesByAdminOnly(boolean adminOnly);
    List<RightType> getRightTypesByIdIn(List<Integer> ids);

    List<RightType> getRightTypesByLevelLessThan(int level);

    List<RightType> getRightTypesByValueIn(List<ERightType> values);
}
