package com.baling.service.right;

import com.baling.models.log.Log;
import com.baling.models.right.ERightType;
import com.baling.models.right.Right;
import com.baling.models.right.UserRight;
import com.baling.models.user.*;
import com.baling.payload.response.DataResponse;
import com.baling.repository.log.LogRepository;
import com.baling.repository.right.RightTypeRepository;
import com.baling.repository.user.MemberRepository;
import com.baling.repository.right.RightRepository;
import com.baling.repository.user.UserRepository;
import com.baling.repository.right.UserRightRepository;
import com.baling.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    LogRepository logRepository;

    @Autowired
    RightTypeRepository rightTypeRepository;

    @Override
    public DataResponse getMembers(Integer rightId,int page) {
        Right right=rightRepository.getById(rightId);


        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, 50, sort);
        Page<UserRight> rightPage=userRightRepository.getUserRightPageByRight(right,pageable);
        List<UserRight> userRights=rightPage.getContent();
        List<Map> members=new ArrayList<>();
        for(UserRight ur:userRights){
            Map m=new HashMap();
            m.put("member",ur.getMember());
            m.put("createTime",ur.getCreateTime());
            m.put("updateTime",ur.getUpdateTime());
            members.add(m);
        }
        Map retMap=new HashMap();
        retMap.put("totalPages",rightPage.getTotalPages());
        retMap.put("data",members);
        return CommonMethod.getReturnData(retMap);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteMember(Integer memberId, Integer rightId) {
        Log log=new Log(getCurrentUser(),rightTypeRepository.getByValue(ERightType.RIGHT_WARRANT),"去除权限");
        try{
            Member member=memberRepository.getById(memberId);
            Right right=rightRepository.getById(rightId);
            userRightRepository.deleteByMemberAndRight(member,right);
            log.setDescription("去除用户："+member.getName()+" 的"+right.getName()+"权限");
            log.setOperateState(0);
            logRepository.save(log);
            return ResponseEntity.ok("Deleted");
        }catch (Exception e){
            log.setOperateState(1);
            logRepository.save(log);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addMember(Integer memberId, Integer rightId) {
        Log log=new Log(getCurrentUser(),rightTypeRepository.getByValue(ERightType.RIGHT_WARRANT),"添加权限");
        try{
            Member member=memberRepository.getById(memberId);
            Right right=rightRepository.getById(rightId);
            UserRight userRight=new UserRight(member,right);
            userRightRepository.save(userRight);
            log.setDescription("给予用户："+member.getName()+" 权限："+right.getName());
            log.setOperateState(0);
            logRepository.save(log);
            return ResponseEntity.ok("Added");
        }catch (Exception e){
            log.setOperateState(1);
            logRepository.save(log);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public DataResponse getMemberRights(int page) {
        Integer userId=CommonMethod.getUserId();
        User user=userRepository.findByUserId(userId).get();
        Member member=memberRepository.getMemberByUser(user);

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, 50, sort);
        Page<UserRight> rightPage=userRightRepository.getUserRightPageByMember(member,pageable);
        List<UserRight> userRights=rightPage.getContent();
        Set<Right> rights=new HashSet<>();
        for(UserRight userRight:userRights)rights.add(userRight.getRight());
        Map retMap=new HashMap();
        retMap.put("totalPages",rightPage.getTotalPages());
        retMap.put("data",rights);
        return CommonMethod.getReturnData(retMap);
    }

    private User getCurrentUser(){
        Integer userId = CommonMethod.getUserId();
        User user = userRepository.findByUserId(userId).get();
        return user;
    }
}
