package com.baling.service.info;

import com.baling.payload.response.DataResponse;


public interface InfoService {
    DataResponse getRightTypeList();

    DataResponse getAllRightTypeList();

    DataResponse getMemberList(String condition);

    DataResponse getLogisticsStatus();

    DataResponse getOrderStatus();
}
