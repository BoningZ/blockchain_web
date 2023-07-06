package com.baling.repository.log;

import com.baling.models.log.Log;
import com.baling.models.right.RightType;
import com.baling.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {
    Page<Log> getLogPageByRightTypeIn(List<RightType> rightTypes,Pageable pageable);
    Page<Log> getLogPageByRightTypeInAndDescriptionLikeAndOperateStateAndOperateTimeBetweenAndUser(List<RightType> rightTypes,
                                                                                                   String description,
                                                                                                   Integer operateState,
                                                                                                   Date start, Date end,
                                                                                                   User user,
                                                                                                   Pageable pageable);
    Page<Log> getLogPageByRightTypeInAndDescriptionLikeAndOperateStateAndOperateTimeBetween(List<RightType> rightTypes,
                                                                                           String description,
                                                                                           Integer operateState,
                                                                                           Date start, Date end,
                                                                                           Pageable pageable);
    Page<Log> getLogPageByRightTypeInAndDescriptionLikeAndOperateTimeBetweenAndUser(List<RightType> rightTypes,
                                                                                   String description,
                                                                                   Date start, Date end,
                                                                                   User user,
                                                                                   Pageable pageable);
    Page<Log> getLogPageByRightTypeInAndDescriptionLikeAndOperateTimeBetween(List<RightType> rightTypes,
                                                                            String description,
                                                                            Date start, Date end,
                                                                            Pageable pageable);
}
