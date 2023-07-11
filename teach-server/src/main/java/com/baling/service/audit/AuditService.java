package com.baling.service.audit;


import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import org.springframework.http.ResponseEntity;




public interface AuditService {
    DataResponse searchLogs(DataRequest dataRequest);
    ResponseEntity<?> generateHash(DataRequest dataRequest);
    DataResponse audit(Integer logId);


}
