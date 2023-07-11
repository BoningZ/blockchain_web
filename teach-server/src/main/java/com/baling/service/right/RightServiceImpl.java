package com.baling.service.right;

import com.baling.models.log.Log;
import com.baling.models.right.ERightType;
import com.baling.models.right.Right;
import com.baling.models.right.RightType;
import com.baling.models.sys_menu.TypeMenu;
import com.baling.models.user.*;
import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.repository.log.LogRepository;
import com.baling.repository.right.RightTypeRepository;
import com.baling.repository.sys_menu.TypeMenuRepository;
import com.baling.repository.user.AdminRepository;
import com.baling.repository.right.RightRepository;
import com.baling.repository.user.UserRepository;
import com.baling.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RightServiceImpl implements RightService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    TypeMenuRepository typeMenuRepository;

    @Autowired
    RightRepository rightRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    RightTypeRepository rightTypeRepository;

    @Autowired
    LogRepository logRepository;



    @Override
    public DataResponse getMenuList(DataRequest dataRequest) {
        Integer userId= CommonMethod.getUserId();
        User user;
        Optional<User> tmp = userRepository.findByUserId(userId);
        user = tmp.get();
        List mList = new ArrayList();
        Map m;
        List<TypeMenu> menus=typeMenuRepository.getTypeMenusByUserType(user.getUserType());
        for(TypeMenu tm:menus){
            m=new HashMap();
            m.put("name",tm.getSysMenu().getUrl());
            m.put("title",tm.getSysMenu().getName());
            mList.add(m);
        }
        return CommonMethod.getReturnData(mList);
    }

    @Override
    public DataResponse getRightList(DataRequest dataRequest) {
        List<Integer> types=(List<Integer>) dataRequest.getList("types");
        String name=dataRequest.getString("name");
        int page=dataRequest.getInteger("page");
        Integer userId=CommonMethod.getUserId();
        User user=userRepository.findByUserId(userId).get();
        Admin admin=adminRepository.getAdminByUser(user);

        Page<Right> rightPage;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, 12, sort);
        if(types!=null&&types.size()>0) {
            List<RightType> rightTypes=rightTypeRepository.getRightTypesByIdIn(types);
            rightPage = rightRepository. getRightPageByAdminAndRightTypesInAndNameLike(admin, rightTypes, "%" + name + "%",pageable);
        }else rightPage=rightRepository.getRightPageByAdminAndNameLike(admin,"%"+name+"%",pageable);
        List<Right> rights=rightPage.getContent();
        List<Map> dataList=new ArrayList<>();
        for(Right right:rights){
            Map rightMap=new HashMap();
            rightMap.put("name",right.getName());
            rightMap.put("createTime",right.getCreateTime());
            rightMap.put("updateTime",right.getUpdateTime());
            rightMap.put("id",right.getId());

            List<Integer> typeList=new ArrayList<>();
            for(RightType rt:right.getRightTypes())typeList.add(rt.getId());
            rightMap.put("types",typeList);
            dataList.add(rightMap);
        }
        Map m=new HashMap();
        m.put("totalPages",rightPage.getTotalPages());
        m.put("data",dataList);
        return CommonMethod.getReturnData(m);
    }

    @Override
    public ResponseEntity<?> deleteRight(Integer id) {
        Integer userId = CommonMethod.getUserId();
        User user = userRepository.findByUserId(userId).get();
        Log log=new Log(user,rightTypeRepository.getByValue(ERightType.RIGHT_DELETE_RIGHT),"删除权限："+id);
        logRepository.save(log);
        try {
            rightRepository.deleteById(id);
            log.setOperateState(0);
            logRepository.save(log);
            return ResponseEntity.ok("Delete Succeeded");
        }catch (Exception e){
            log.setOperateState(1);
            logRepository.save(log);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addRight(DataRequest dataRequest) {
        Integer userId = CommonMethod.getUserId();
        User user = userRepository.findByUserId(userId).get();
        Admin admin = adminRepository.getAdminByUser(user);
        Log log=new Log(user,rightTypeRepository.getByValue(ERightType.RIGHT_ADD_RIGHT),"添加权限");
        logRepository.save(log);
        try {
            Right right = new Right();
            right.setAdmin(admin);
            String name=dataRequest.getString("name");
            right.setName(name);
            log.setDescription("添加权限："+name);
            right.setRightTypes(rightTypeRepository.getRightTypesByIdIn((List<Integer>) dataRequest.getList("types")));
            right.setCreateTime(new Date());
            right.setUpdateTime(new Date());

            rightRepository.save(right);

            log.setOperateState(0);
            logRepository.save(log);
            return ResponseEntity.ok("Save Succeeded");
        }catch (Exception e){
            log.setOperateState(1);
            logRepository.save(log);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateRight(DataRequest dataRequest) {
        Integer userId = CommonMethod.getUserId();
        User user = userRepository.findByUserId(userId).get();
        Admin admin = adminRepository.getAdminByUser(user);
        Log log=new Log(user,rightTypeRepository.getByValue(ERightType.RIGHT_UPDATE_RIGHT),"修改权限");
        logRepository.save(log);
        try {
            Right right = rightRepository.getById(dataRequest.getInteger("id"));

            right.setName(dataRequest.getString("name"));
            log.setDescription("修改权限："+right.getName());
            right.setRightTypes(rightTypeRepository.getRightTypesByIdIn((List<Integer>) dataRequest.getList("types")));
            right.setUpdateTime(new Date());

            rightRepository.save(right);

            log.setOperateState(0);
            logRepository.save(log);
            return ResponseEntity.ok("Update Succeeded");
        }catch (Exception e){
            log.setOperateState(1);
            logRepository.save(log);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public DataResponse getRightsMap() {
        Integer userId=CommonMethod.getUserId();
        User user=userRepository.findByUserId(userId).get();
        Admin admin=adminRepository.getAdminByUser(user);

        List<Right> rights=rightRepository.getRightsByAdmin(admin);
        List<Map> dataList=new ArrayList<>();
        for(Right right:rights){
            Map m=new HashMap();
            m.put("id",right.getId());
            m.put("name",right.getName());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }


}
