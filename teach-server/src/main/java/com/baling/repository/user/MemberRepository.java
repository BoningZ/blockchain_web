package com.baling.repository.user;

import com.baling.models.user.Member;
import com.baling.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member,Integer> {
    Boolean existsByMid(String mid);
    Member getMemberByUser(User user);
    List<Member> getMembersBy();
    Page<Member> getMemberPageBy(Pageable pageable);
    List<Member> getMembersByMidLikeOrNameLike(String mid,String name);

    Page<Member> getMemberPageByMidLikeOrNameLike(String mid,String name,Pageable pageable);
}
