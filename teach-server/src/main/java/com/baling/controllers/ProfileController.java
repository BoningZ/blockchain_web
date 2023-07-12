package com.baling.controllers;

import com.baling.models.user.EUserType;
import com.baling.models.user.Member;
import com.baling.models.user.Admin;
import com.baling.models.user.User;
import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.repository.user.MemberRepository;
import com.baling.repository.user.AdminRepository;
import com.baling.repository.user.UserRepository;
import com.baling.service.profile.ProfileService;
import com.baling.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @PostMapping("/getProfile")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public DataResponse getProfile(@Valid @RequestBody DataRequest dataRequest){
        return profileService.getProfile(dataRequest);
    }

    @PostMapping("/submitProfile")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public DataResponse submitProfile(@Valid @RequestBody DataRequest dataRequest){
        return profileService.submitProfile(dataRequest);
    }

    @GetMapping("/getBasicInfo")
    public DataResponse getBasicInfo(){
        return profileService.getBasicInfo();
    }

}

