package com.baling.service.right;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RightService {
    DataResponse getMenuList(DataRequest dataRequest);
    DataResponse getRightList(DataRequest dataRequest);
    ResponseEntity<?> deleteRight(Integer id);
    ResponseEntity<?> addRight(DataRequest dataRequest);
    ResponseEntity<?> updateRight(DataRequest dataRequest);
    DataResponse getRightsMap();


}
