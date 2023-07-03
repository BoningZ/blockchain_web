package com.baling.service.blockchain;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import org.springframework.http.ResponseEntity;

public interface ChainDataService {
    DataResponse getTxnById(String id);
    DataResponse searchTxs(String startDateTime,String endDateTime,String buyerId,String sellerId,String logisticsStatus,String orderStatus);
    ResponseEntity<?> createTxn(DataRequest dataRequest);
    ResponseEntity<?> updateTxn(DataRequest dataRequest);
    ResponseEntity<?> addBuyerReview(DataRequest dataRequest);
    ResponseEntity<?> addSellerReview(DataRequest dataRequest);
}
