package com.baling.service.right;

import com.baling.models.sys_menu.TypeMenu;
import com.baling.models.user.*;
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
        User user=userRepository.findByUserId(userId).get();
        Admin admin=adminRepository.getAdminByUser(user);

        List<Right> rights;
        if(type!=null&&!type.equals("")) {
            ERightType rightType = ERightType.valueOf(type);
            rights = rightRepository.getRightsByAdminAndTypeAndNameLike(admin, rightType, "%" + name + "%");
        }else rights=rightRepository.getRightsByAdminAndNameLike(admin,"%"+name+"%");
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
            User user = userRepository.findByUserId(userId).get();
            Admin admin = adminRepository.getAdminByUser(user);

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

    @Override
    public DataResponse getRightTypeList() {
        List list=new ArrayList();
        Map m=new HashMap();
        m.put("value","RIGHT_DELETE");
        m.put("label","删除交易");
        list.add(m);

        m=new HashMap();
        m.put("value","RIGHT_ADD");
        m.put("label","创建交易");
        list.add(m);

        m=new HashMap();
        m.put("value","RIGHT_UPDATE_STATUS");
        m.put("label","更改交易状态");
        list.add(m);

        m=new HashMap();
        m.put("value","RIGHT_UPDATE_BUYER");
        m.put("label","添加买家评价");
        list.add(m);

        m=new HashMap();
        m.put("value","RIGHT_UPDATE_SELLER");
        m.put("label","添加卖家评价");
        list.add(m);

        m=new HashMap();
        m.put("value","RIGHT_UPDATE_LOGISTICS");
        m.put("label","更新物流信息");
        list.add(m);

        m=new HashMap();
        m.put("value","RIGHT_QUERY");
        m.put("label","查询交易");
        list.add(m);

        m=new HashMap();
        m.put("value","RIGHT_QUERY_HISTORY");
        m.put("label","查询交易历史");
        list.add(m);

        return CommonMethod.getReturnData(list);
    }
}
