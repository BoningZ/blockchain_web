package com.baling.service.profile;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;

public interface ProfileService {
    DataResponse getProfile(DataRequest dataRequest);
    DataResponse submitProfile(DataRequest dataRequest);
}
