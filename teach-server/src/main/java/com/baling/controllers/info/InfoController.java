package com.baling.controllers.info;

import com.baling.payload.response.DataResponse;
import com.baling.service.info.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/getMemberList")
    public DataResponse getMemberList(){
        return infoService.getMemberList();
    }
}
