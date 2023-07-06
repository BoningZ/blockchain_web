package com.baling.repository.right;

import com.baling.models.user.Member;
import com.baling.models.right.Right;
import com.baling.models.right.UserRight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface UserRightRepository extends JpaRepository<UserRight,Integer> {
    @Modifying
    void deleteByMemberAndRight(Member member, Right right);
    List<UserRight> getUserRightsByRight(Right right);
    Page<UserRight> getUserRightPageByRight(Right right, Pageable pageable);
    List<UserRight> getUserRightsByMember(Member member);
    Page<UserRight> getUserRightPageByMember(Member member,Pageable pageable);



}
