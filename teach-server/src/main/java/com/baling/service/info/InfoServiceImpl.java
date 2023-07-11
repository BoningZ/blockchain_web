package com.baling.service.info;

import com.baling.payload.response.DataResponse;
import com.baling.repository.right.RightTypeRepository;
import com.baling.repository.status.LogisticsStatusRepository;
import com.baling.repository.status.OrderStatusRepository;
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
    @Autowired
    LogisticsStatusRepository logisticsStatusRepository;
    @Autowired
    OrderStatusRepository orderStatusRepository;
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

    @Override
    public DataResponse getLogisticsStatus() {
        return CommonMethod.getReturnData(logisticsStatusRepository.getAllBy());
    }

    @Override
    public DataResponse getOrderStatus() {
        return CommonMethod.getReturnData(orderStatusRepository.getAllBy());
    }
}
