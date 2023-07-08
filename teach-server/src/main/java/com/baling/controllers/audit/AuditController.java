package com.baling.controllers.audit;


import com.baling.models.right.RightType;
import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.service.audit.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/audit")
public class AuditController {
    @Autowired
    AuditService auditService;

    @PostMapping("/searchLogs")
    public DataResponse searchLogs(@Valid @RequestBody DataRequest dataRequest){
        return auditService.searchLogs(dataRequest);
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
