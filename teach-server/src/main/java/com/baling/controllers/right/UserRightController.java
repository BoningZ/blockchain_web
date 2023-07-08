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

    /**
     * 获取当前登录人的权限列表，仅限成员角色
     * @return 权限列表
     */
    @GetMapping("/getMemberRights")
    public DataResponse getMemberRights(@RequestParam("page")int page){
        return userRightService.getMemberRights(page);
    }

    /**
     * 获取当前登录人的基本权限列表，仅限成员角色
     * @return 基本权限列表
     */
    @GetMapping("/getMyRightTypes")
    public DataResponse getMyRightTypes(){
        return userRightService.getMyRightTypes();
    }

    /**
     * 查询某成员具有的权限列表，只包括当前登录管理员创建的权限
     * @param memberId 要查询的成员Id
     * @return 该成员具有的、当前登录用户创建的权限列表
     */
    @GetMapping("/getRightsByMember")
    public DataResponse getRightsByMember(@RequestParam("memberId")Integer memberId){
        return userRightService.getRightsByMember(memberId);
    }

    /**
     * 更新用户的权限
     * @param dataRequest 包括成员Id、权限Id列表
     * @return 处理状态
     */
    @PutMapping("/updateRightsByMember")
    public ResponseEntity<?> updateRightsByMember(@Valid @RequestBody DataRequest dataRequest){
        return userRightService.updateRightsForMember(dataRequest);
    }

    /**
     * 获取“成员-当前登录管理员创建的权限集"列表
     * @param page 第几页
     * @return “成员-权限集"列表
     */
    @GetMapping("/getRightsOfAll")
    public DataResponse getRightsOfAll(@RequestParam("page")Integer page){
        return userRightService.getRightsOfAll(page);
    }
}
