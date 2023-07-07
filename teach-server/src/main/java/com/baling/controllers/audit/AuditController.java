package com.baling.controllers.audit;


import com.baling.models.right.RightType;
import com.baling.payload.response.DataResponse;
import com.baling.service.audit.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/audit")
public class AuditController {
    @Autowired
    AuditService auditService;

    @GetMapping("/searchLogs")
    public DataResponse searchLogs(@RequestParam("rightTypes") List<String> rightTypes,
                                   @RequestParam(value = "maxLevel",required = false) Integer maxLevel,
                                   @RequestParam(value = "startTime",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")Date startTime,
                                   @RequestParam(value = "endTime",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")Date endTime,
                                   @RequestParam(value = "operateState",required = false) Integer operateState,
                                   @RequestParam("description") String description,
                                   @RequestParam(value = "userId",required = false) Integer userId,
                                   @RequestParam("page") int page){
        return auditService.searchLogs(rightTypes,maxLevel,startTime,endTime,operateState,description,userId,page);
    }

    @PutMapping("/generateHash")
    public ResponseEntity<?> generateHash(@RequestParam("logId")Integer logId){
        return auditService.generateHash(logId);
    }

    @GetMapping("/audit")
    public DataResponse audit(@RequestParam("logId")Integer logId){
        return auditService.audit(logId);
    }

}
