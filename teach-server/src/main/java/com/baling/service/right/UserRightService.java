package com.baling.service.right;

import com.baling.payload.response.DataResponse;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;

public interface UserRightService {
    DataResponse getMembers(Integer rightId,int page);
    ResponseEntity<?> deleteMember(Integer memberId,Integer rightId);
    ResponseEntity<?> addMember(Integer memberId, Integer rightId);
    DataResponse getMemberRights(int page);
}
