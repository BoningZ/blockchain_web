package com.baling.service.info;

import com.baling.payload.response.DataResponse;
import com.baling.repository.right.RightTypeRepository;
import com.baling.repository.user.MemberRepository;
import com.baling.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoServiceImpl implements InfoService{
    @Autowired
    RightTypeRepository rightTypeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Override
    public DataResponse getRightTypeList() {
        return CommonMethod.getReturnData(rightTypeRepository.getRightTypesByAdminOnly(false));
    }

    @Override
    public DataResponse getAllRightTypeList() {
        return CommonMethod.getReturnData(rightTypeRepository.getAllBy());
    }

    @Override
    public DataResponse getMemberList(String condition) {
        return CommonMethod.getReturnData(memberRepository.getMembersByMidLikeOrNameLike("%"+condition+"%","%"+condition+"%"));
    }
}
