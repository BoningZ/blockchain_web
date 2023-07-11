package com.baling.controllers.info;

import com.baling.payload.response.DataResponse;
import com.baling.service.info.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/info")
public class InfoController {
    @Autowired
    InfoService infoService;

    @GetMapping("/getRightTypeList")
    public DataResponse getRightTypeList(){
        return infoService.getRightTypeList();
    }

    @GetMapping("/getAllRightTypeList")
    public DataResponse getAllRightTypeList(){
        return infoService.getAllRightTypeList();
    }

    @GetMapping("/getMemberList")
    public DataResponse getMemberList(@RequestParam("condition")String condition){
        return infoService.getMemberList(condition);
    }
    @GetMapping("/getLogisticsStatus")
    public DataResponse getLogisticsStatus(){
        return infoService.getLogisticsStatus();
    }
    @GetMapping("/getOrderStatus")
    public DataResponse getOrderStatus(){
        return infoService.getOrderStatus();
    }
}
