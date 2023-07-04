package com.baling.repository.right;

import com.baling.models.user.Member;
import com.baling.models.right.Right;
import com.baling.models.right.UserRight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface UserRightRepository extends JpaRepository<UserRight,Integer> {
    @Modifying
    void deleteByMemberAndRight(Member member, Right right);
    List<UserRight> getUserRightsByRight(Right right);
    List<UserRight> getUserRightsByMember(Member member);



}
