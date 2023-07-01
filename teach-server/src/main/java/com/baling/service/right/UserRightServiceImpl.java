package com.baling.service.right;

import com.baling.models.user.Member;
import com.baling.models.user.Right;
import com.baling.models.user.UserRight;
import com.baling.payload.response.DataResponse;
import com.baling.repository.user.MemberRepository;
import com.baling.repository.user.RightRepository;
import com.baling.repository.user.UserRightRepository;
import com.baling.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserRightServiceImpl implements UserRightService{
    @Autowired
    RightRepository rightRepository;
    @Autowired
    UserRightRepository userRightRepository;
    @Autowired
    MemberRepository memberRepository;

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
}
