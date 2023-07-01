package com.baling.service.profile;

import com.baling.models.user.Admin;
import com.baling.models.user.EUserType;
import com.baling.models.user.Member;
import com.baling.models.user.User;
import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.repository.user.AdminRepository;
import com.baling.repository.user.MemberRepository;
import com.baling.repository.user.UserRepository;
import com.baling.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    MemberRepository memberRepository;

    @Override
    public DataResponse getProfile(DataRequest dataRequest) {
        Integer userId= CommonMethod.getUserId();
        User user;
        Optional<User> tmp = userRepository.findByUserId(userId);
        user = tmp.get();
        Map m=new HashMap();
        if(user.getUserType().getName()== EUserType.ROLE_ADMIN){
            Admin admin =adminRepository.getAdminByUser(user);
            m.put("aid", admin.getAid());
            m.put("name", admin.getName());
        }else{
            Member member =memberRepository.getMemberByUser(user);
            m.put("mid", member.getMid());
            m.put("name", member.getName());
        }
        return CommonMethod.getReturnData(m);
    }

    @Override
    public DataResponse submitProfile(DataRequest dataRequest) {
        Integer userId= CommonMethod.getUserId();
        User user;
        Optional<User> tmp = userRepository.findByUserId(userId);
        user = tmp.get();
        if(user.getUserType().getName()== EUserType.ROLE_ADMIN){
            Admin admin =adminRepository.getAdminByUser(user);
            admin.setAid(dataRequest.getString("aid"));
            admin.setName(dataRequest.getString("name"));
            adminRepository.save(admin);
        }else{
            Member member =memberRepository.getMemberByUser(user);
            member.setMid(dataRequest.getString("mid"));
            member.setName(dataRequest.getString("name"));
            memberRepository.save(member);
        }
        return CommonMethod.getReturnMessageOK();
    }
}