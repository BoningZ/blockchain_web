package com.baling.controllers.right;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.service.right.UserRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/right")
public class UserRightController {
    @Autowired
    UserRightService userRightService;

    @GetMapping("/getMembers")
    public DataResponse getUsers(@RequestParam("rightId")Integer id,@RequestParam("page")int page){
        return userRightService.getMembers(id,page);
    }

    @DeleteMapping("/deleteMember")
    public ResponseEntity<?> deleteMember(@RequestParam("memberId")Integer memberId, @RequestParam("rightId")Integer rightId){
        return userRightService.deleteMember(memberId,rightId);
    }

    @PutMapping("/addMember")
    public ResponseEntity<?> addMember(@RequestParam("memberId")Integer memberId, @RequestParam("rightId")Integer rightId){
        return userRightService.addMember(memberId,rightId);
    }

    @GetMapping("/getMemberRights")
    public DataResponse getMemberRights(@RequestParam("page")int page){
        return userRightService.getMemberRights(page);
    }

    @GetMapping("/getMyRightTypes")
    public DataResponse getMyRightTypes(){
        return userRightService.getMyRightTypes();
    }

    @GetMapping("/getRightsByMember")
    public DataResponse getRightsByMember(@RequestParam("memberId")Integer memberId){
        return userRightService.getRightsByMember(memberId);
    }

    @PutMapping("/updateRightsByMember")
    public ResponseEntity<?> updateRightsByMember(@Valid @RequestBody DataRequest dataRequest){
        return userRightService.updateRightsForMember(dataRequest);
    }

    @GetMapping("/getRightsOfAll")
    public DataResponse getRightsOfAll(@RequestParam("page")Integer page){
        return userRightService.getRightsOfAll(page);
    }
}
