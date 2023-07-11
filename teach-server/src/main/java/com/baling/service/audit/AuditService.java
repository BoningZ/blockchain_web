package com.baling.service.audit;

import com.baling.models.right.RightType;
import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;


public interface AuditService {
    DataResponse searchLogs(DataRequest dataRequest);
    ResponseEntity<?> generateHash(DataRequest dataRequest);
    DataResponse audit(Integer logId);


}
