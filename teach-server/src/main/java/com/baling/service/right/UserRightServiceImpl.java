package com.baling.service.right;

import com.baling.models.right.Right;
import com.baling.models.right.UserRight;
import com.baling.models.user.*;
import com.baling.payload.response.DataResponse;
import com.baling.repository.user.MemberRepository;
import com.baling.repository.right.RightRepository;
import com.baling.repository.user.UserRepository;
import com.baling.repository.right.UserRightRepository;
import com.baling.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
public class UserRightServiceImpl implements UserRightService{
    @Autowired
    RightRepository rightRepository;
    @Autowired
    UserRightRepository userRightRepository;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public DataResponse getMembers(Integer rightId) {
        Right right=rightRepository.getById(rightId);
        List<UserRight> userRights=userRightRepository.getUserRightsByRight(right);
        List<Map> members=new ArrayList<>();
        for(UserRight ur:userRights){
            Map m=new HashMap();
            m.put("member",ur.getMember());
            m.put("createTime",ur.getCreateTime());
            m.put("updateTime",ur.getUpdateTime());
            members.add(m);
        }
        return CommonMethod.getReturnData(members);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteMember(Integer memberId, Integer rightId) {
        try{
            Member member=memberRepository.getById(memberId);
            Right right=rightRepository.getById(rightId);
            userRightRepository.deleteByMemberAndRight(member,right);
            return ResponseEntity.ok("Deleted");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addMember(Integer memberId, Integer rightId) {
        try{
            Member member=memberRepository.getById(memberId);
            Right right=rightRepository.getById(rightId);
            UserRight userRight=new UserRight(member,right);
            userRightRepository.save(userRight);
            return ResponseEntity.ok("Added");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public DataResponse getMemberRights() {
        Integer userId=CommonMethod.getUserId();
        User user=userRepository.findByUserId(userId).get();
        Member member=memberRepository.getMemberByUser(user);
        List<UserRight> userRights=userRightRepository.getUserRightsByMember(member);
        Set<Right> rights=new HashSet<>();
        for(UserRight userRight:userRights)rights.add(userRight.getRight());
        return CommonMethod.getReturnData(rights);
    }
}
