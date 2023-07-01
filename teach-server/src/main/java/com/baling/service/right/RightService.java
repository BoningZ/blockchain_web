package com.baling.service.right;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;

public interface RightService {
    DataResponse getMenuList(DataRequest dataRequest);
}
