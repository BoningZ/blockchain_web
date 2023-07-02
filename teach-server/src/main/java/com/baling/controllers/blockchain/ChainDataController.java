package com.baling.controllers.blockchain;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.service.blockchain.ChainDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/chainData")
public class ChainDataController {
    @Autowired
    ChainDataService chainDataService;

    @GetMapping("/getTxn")
    public DataResponse getTxn(@RequestParam("orderId")String orderId){
        return chainDataService.getTxnById(orderId);
    }

    @PutMapping("/createTxn")
    public ResponseEntity<?> createTxn(@Valid @RequestBody DataRequest dataRequest){
        return chainDataService.createTxn(dataRequest);
    }

    @PutMapping("/updateTxn")
    public ResponseEntity<?> updateTxn(@Valid @RequestBody DataRequest dataRequest){
        return chainDataService.updateTxn(dataRequest);
    }

    @PutMapping("/addBuyerReview")
    public ResponseEntity<?> addBuyerReview(@Valid @RequestBody DataRequest dataRequest){
        return chainDataService.addBuyerReview(dataRequest);
    }

    @PutMapping("/addSellerReview")
    public ResponseEntity<?> addSellerReview(@Valid @RequestBody DataRequest dataRequest){
        return chainDataService.addSellerReview(dataRequest);
    }
}
