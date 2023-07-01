package com.baling.service.right;

import com.baling.models.sys_menu.TypeMenu;
import com.baling.models.user.Admin;
import com.baling.models.user.ERightType;
import com.baling.models.user.Right;
import com.baling.models.user.User;
import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.repository.sys_menu.TypeMenuRepository;
import com.baling.repository.user.AdminRepository;
import com.baling.repository.user.RightRepository;
import com.baling.repository.user.UserRepository;
import com.baling.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
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
    public DataResponse getRightList(String type,String name) {
        Integer userId=CommonMethod.getUserId();
        User uesr=userRepository.findByUserId(userId).get();
        Admin admin=adminRepository.getAdminByUser(uesr);

        ERightType rightType=ERightType.valueOf(type);
        List<Right> rights=rightRepository.getRightsByAdminAndTypeAndNameLike(admin,rightType,name);
        return CommonMethod.getReturnData(rights);
    }

    @Override
    public ResponseEntity<?> deleteRight(Integer id) {
        try {
            rightRepository.deleteById(id);
            return ResponseEntity.ok("Delete Succeeded");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addRight(DataRequest dataRequest) {
        try {
            Integer userId = CommonMethod.getUserId();
            User uesr = userRepository.findByUserId(userId).get();
            Admin admin = adminRepository.getAdminByUser(uesr);

            Right right = new Right();
            right.setAdmin(admin);
            right.setName(dataRequest.getString("name"));
            right.setType(ERightType.valueOf(dataRequest.getString("type")));
            right.setCreateTime(new Date());
            right.setUpdateTime(new Date());

            rightRepository.save(right);

            return ResponseEntity.ok("Save Succeeded");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
