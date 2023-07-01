package com.baling.controllers;


import javax.validation.Valid;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;

import com.baling.service.auth.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baling.payload.request.LoginRequest;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticate(loginRequest);
    }

    @PostMapping("/signup")
    public DataResponse registerUser(@Valid @RequestBody DataRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }


}
