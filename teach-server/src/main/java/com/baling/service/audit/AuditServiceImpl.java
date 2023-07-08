package com.baling.service.audit;

import com.baling.models.log.Log;
import com.baling.models.right.ERightType;
import com.baling.models.right.RightType;
import com.baling.models.user.User;
import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.repository.log.LogRepository;
import com.baling.repository.right.RightTypeRepository;
import com.baling.repository.user.UserRepository;
import com.baling.util.CommonMethod;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuditServiceImpl implements AuditService{
    @Autowired
    LogRepository logRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RightTypeRepository rightTypeRepository;

    @Override
    public DataResponse searchLogs(DataRequest dataRequest) {
        List<String> rightTypeNames=(List<String>) dataRequest.getList("rightTypeNames");
        Integer maxLevel=dataRequest.getInteger("maxLevel");
        Date startTime=dataRequest.getDate("startTime"), endTime=dataRequest.getDate("endTime");
        Integer operateState=dataRequest.getInteger("operateState");
        String description=dataRequest.getString("description");
        Integer userId=dataRequest.getInteger("userId");
        Integer page=dataRequest.getInteger("page");

        User u=null;
        if(userId!=null)u=userRepository.findByUserId(userId).get();

        if(startTime==null)startTime=new Date(0);
        if(endTime==null)endTime=new Date(2147483647000L);
            else endTime= DateUtils.truncate(DateUtils.addDays(endTime, 1), Calendar.DATE);//change to tomorrow midnight

        List<RightType> prepareRightTypes;
        if(rightTypeNames!=null&&rightTypeNames.size()>0) {
            List<ERightType> eRightTypes = new ArrayList<>();
            for (String rt : rightTypeNames) eRightTypes.add(ERightType.valueOf(rt));
            prepareRightTypes = rightTypeRepository.getRightTypesByValueIn(eRightTypes);
        }else prepareRightTypes = rightTypeRepository.getAllBy();

        List<RightType> rightTypes=new ArrayList<>();
        for(RightType rightType:prepareRightTypes){
            if (maxLevel == null || rightType.getLevel() <= maxLevel)
                rightTypes.add(rightType);
        }

        if(description==null)description="";

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, 50, sort);

        Page<Log> logPage;
        if(u!=null&&operateState!=null)
            logPage=logRepository.getLogPageByRightTypeInAndDescriptionLikeAndOperateStateAndOperateTimeBetweenAndUser(rightTypes,"%"+description+"%",operateState,startTime,endTime,u,pageable);
        else if(u==null&&operateState!=null)
            logPage=logRepository.getLogPageByRightTypeInAndDescriptionLikeAndOperateStateAndOperateTimeBetween(rightTypes,"%"+description+"%",operateState,startTime,endTime,pageable);
        else if(u!=null)
            logPage=logRepository.getLogPageByRightTypeInAndDescriptionLikeAndOperateTimeBetweenAndUser(rightTypes,"%"+description+"%",startTime,endTime,u,pageable);
        else logPage=logRepository.getLogPageByRightTypeInAndDescriptionLikeAndOperateTimeBetween(rightTypes,"%"+description+"%",startTime,endTime,pageable);

        Map retMap=new HashMap<>();
        retMap.put("totalPages",logPage.getTotalPages());
        retMap.put("data",logPage.getContent());
        return CommonMethod.getReturnData(retMap);
    }

    @Override
    public ResponseEntity<?> generateHash(Integer logId) {
        try {
            Log log=logRepository.getById(logId);
            log.generateHash();
            logRepository.save(log);
            return ResponseEntity.ok("generated");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public DataResponse audit(Integer logId) {
        Log log=logRepository.getById(logId);
        if(log.getHash()==null)return CommonMethod.getReturnMessageError("无杂凑值！");
        return CommonMethod.getReturnData(log.audit());
    }
}
