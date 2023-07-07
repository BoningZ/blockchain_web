package com.baling.service.blockchain;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import org.springframework.http.ResponseEntity;

public interface ChainDataService {
    DataResponse getTxnById(String id);
    DataResponse getTxnHistoryById(String id);
    DataResponse searchTxs(DataRequest dataRequest);
    ResponseEntity<?> createTxn(DataRequest dataRequest);
    ResponseEntity<?> updateTxn(DataRequest dataRequest);
    ResponseEntity<?> deleteTxn(String id);
    ResponseEntity<?> updateLogistics(DataRequest dataRequest);
    ResponseEntity<?> addBuyerReview(DataRequest dataRequest);
    ResponseEntity<?> addSellerReview(DataRequest dataRequest);
    DataResponse decryptName(String encrypted);
}
