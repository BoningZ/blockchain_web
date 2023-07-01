package com.baling.service.auth;

import com.baling.payload.request.DataRequest;
import com.baling.payload.request.LoginRequest;
import com.baling.payload.response.DataResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> authenticate(LoginRequest loginRequest);
    DataResponse signUp(DataRequest signUpRequest);
    DataResponse changePassword(DataRequest dataRequest);

}
