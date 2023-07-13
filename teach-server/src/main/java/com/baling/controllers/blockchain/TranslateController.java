package com.baling.controllers.blockchain;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.service.blockchain.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/chainData")
public class TranslateController {
    @Autowired
    TranslateService translateService;
    @PostMapping("/translate")
    public DataResponse translate(@Valid @RequestBody DataRequest dataRequest){
        return translateService.translate(dataRequest);
    }
}
