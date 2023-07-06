package com.baling.service.audit;

import com.baling.models.right.RightType;
import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;

import java.util.Date;
import java.util.List;


public interface AuditService {
    DataResponse searchLogs(List<String> rightTypes, Integer maxLevel,
                            Date startTime,Date endTime,
                            Integer operateState,String description,Integer userId,
                            int page);


}
