package com.baling.controllers;

import com.baling.models.sys_menu.TypeMenu;
import com.baling.models.user.EUserType;
import com.baling.models.user.User;
import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.repository.sys_menu.TypeMenuRepository;
import com.baling.repository.user.UserRepository;
import com.baling.service.auth.AuthService;
import com.baling.service.right.RightService;
import com.baling.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class TestController {
    @Autowired
    AuthService authService;

    @Autowired
    RightService rightService;

    @PostMapping("/getMenuList")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public DataResponse getMenuList(@Valid @RequestBody DataRequest dataRequest) {
        return rightService.getMenuList(dataRequest);
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public DataResponse changePassword(@Valid @RequestBody DataRequest dataRequest) {
        return authService.changePassword(dataRequest);
    }

}
