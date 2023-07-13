package com.baling.service.blockchain;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;

public interface TranslateService {
    DataResponse translate(DataRequest dataRequest);
}
