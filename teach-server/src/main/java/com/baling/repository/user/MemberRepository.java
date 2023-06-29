package com.baling.repository.user;

import com.baling.models.user.Member;
import com.baling.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Integer> {
    Boolean existsByMid(String mid);
    Member getMemberByUser(User user);
}
