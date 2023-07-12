package com.baling.controllers.blockchain;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.service.blockchain.ChainDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/chainData")
public class ChainDataController {
    @Autowired
    ChainDataService chainDataService;



    @GetMapping("/getTxnHistory")
    public DataResponse getTxnHistory(@RequestParam("orderId")String orderId){
        return chainDataService.getTxnHistoryById(orderId);
    }

    @PostMapping("/searchTxs")
    public DataResponse searchTxs(@Valid @RequestBody DataRequest dataRequest){
        String orderId=dataRequest.getString("orderId");
        if(orderId!=null&&!orderId.equals(""))return chainDataService.getTxnById(orderId);
        else return chainDataService.searchTxs(dataRequest);
    }

    @PutMapping("/createTxn")
    public ResponseEntity<?> createTxn(@Valid @RequestBody DataRequest dataRequest){
        return chainDataService.createTxn(dataRequest);
    }

    @DeleteMapping("/deleteTxn")
    public ResponseEntity<?> deleteTxn(@RequestParam("orderId")String orderId){
        return chainDataService.deleteTxn(orderId);
    }

    @PostMapping("/deleteTxs")
    public ResponseEntity<?> deleteTxs(@Valid @RequestBody DataRequest dataRequest){
        List orderIds=dataRequest.getList("orderIds");
        try{
            for(Object orderId:orderIds){
                HttpStatus status=chainDataService.deleteTxn((String) orderId).getStatusCode();
                if(status== HttpStatus.INTERNAL_SERVER_ERROR)
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除订单："+orderId+"失败！");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok("deleted");
    }

    @PutMapping("/updateLogistics")
    public ResponseEntity<?> updateLogistics(@Valid @RequestBody DataRequest dataRequest){
        return chainDataService.updateLogistics(dataRequest);
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

    @PostMapping("/decrypt")
    public DataResponse decrypt(@Valid @RequestBody DataRequest dataRequest){
        return chainDataService.decryptName(dataRequest.getString("name"));
    }
}
