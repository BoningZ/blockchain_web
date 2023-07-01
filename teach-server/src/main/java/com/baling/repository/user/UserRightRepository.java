package com.baling.repository.user;

import com.baling.models.user.Member;
import com.baling.models.user.Right;
import com.baling.models.user.UserRight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRightRepository extends JpaRepository<UserRight,Integer> {
    void deleteByMemberAndRight(Member member, Right right);
    List<UserRight> getUserRightsByRight(Right right);



}
