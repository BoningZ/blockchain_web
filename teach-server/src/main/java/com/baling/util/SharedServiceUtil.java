package com.baling.util;

import com.baling.models.right.ERightType;
import com.baling.models.right.Right;
import com.baling.models.right.RightType;
import com.baling.models.right.UserRight;
import com.baling.models.user.EUserType;
import com.baling.models.user.Member;
import com.baling.models.user.User;
import com.baling.repository.right.RightRepository;
import com.baling.repository.right.RightTypeRepository;
import com.baling.repository.right.UserRightRepository;
import com.baling.repository.user.MemberRepository;
import com.baling.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Component
public class SharedServiceUtil {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    UserRightRepository userRightRepository;
    @Autowired
    RightTypeRepository rightTypeRepository;

    public boolean hasRight(ERightType eRightType){
        User u=getCurrentUser();
        if(u.getUserType().getName()==EUserType.ROLE_ADMIN){
            return true;
        }

        RightType rightType=rightTypeRepository.getByValue(eRightType);
        Member m=memberRepository.getMemberByUser(u);

        List<UserRight> userRights=userRightRepository.getUserRightsByMember(m);
        for(UserRight ur:userRights){
            if(ur.getRight().getRightTypes().contains(rightType))return true;
        }
        return false;
    }

    public User getCurrentUser(){
        Integer userId = CommonMethod.getUserId();
        User user = userRepository.findByUserId(userId).get();
        return user;
    }

    public String calculateMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
