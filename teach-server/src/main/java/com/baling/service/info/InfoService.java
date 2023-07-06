package com.baling.service.info;

import com.baling.payload.response.DataResponse;

public interface InfoService {
    DataResponse getRightTypeList();
    DataResponse getMemberList(String condition);
}
