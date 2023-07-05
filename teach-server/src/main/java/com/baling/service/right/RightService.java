package com.baling.service.right;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import org.springframework.http.ResponseEntity;

public interface RightService {
    DataResponse getMenuList(DataRequest dataRequest);
    DataResponse getRightList(String type, String name);
    ResponseEntity<?> deleteRight(Integer id);
    ResponseEntity<?> addRight(DataRequest dataRequest);


}
