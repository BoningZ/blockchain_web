package com.baling.service.auth;

import com.baling.models.log.Log;
import com.baling.models.right.ERightType;
import com.baling.models.user.*;
import com.baling.payload.request.DataRequest;
import com.baling.payload.request.LoginRequest;
import com.baling.payload.response.DataResponse;
import com.baling.payload.response.JwtResponse;
import com.baling.repository.log.LogRepository;
import com.baling.repository.right.RightTypeRepository;
import com.baling.repository.user.AdminRepository;
import com.baling.repository.user.MemberRepository;
import com.baling.repository.user.UserRepository;
import com.baling.repository.user.UserTypeRepository;
import com.baling.security.jwt.JwtUtils;
import com.baling.security.services.UserDetailsImpl;
import com.baling.util.CommonMethod;
import com.baling.util.SharedServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserTypeRepository userTypeRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    LogRepository logRepository;

    @Autowired
    RightTypeRepository rightTypeRepository;

    @Autowired
    SharedServiceUtil sharedServiceUtil;

    @Override
    public ResponseEntity<?> authenticate(LoginRequest loginRequest) {
        Log log=null;
        Optional<User> fakeUser = userRepository.findByUsername(loginRequest.getUsername());
        if(fakeUser.isPresent()){
            log=new Log(fakeUser.get(),rightTypeRepository.getByValue(ERightType.valueOf("RIGHT_LOGIN")),"尝试登录");
            log.setOperateState(1);
            logRepository.save(log);
        }


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        User user=userRepository.findByUsername(userDetails.getUsername()).get();
        user.setLastLoginTime(new Date());
        user.setLoginCount(user.getLoginCount()+1);
        userRepository.save(user);
        if(log!=null){
            log.setOperateState(0);
            logRepository.save(log);
        }

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles.get(0)));
    }

    @Override
    public DataResponse signUp(DataRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getString("username"))) {
            return /*ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"))*/
                    CommonMethod.getReturnMessageError("该用户名已被占用！");
        }

        // Create new user's account
        User user = new User(signUpRequest.getString("username"),
                encoder.encode(signUpRequest.getString("password")));

        String strRoles = signUpRequest.getString("role");


        if (strRoles == null) {
            UserType userRole = userTypeRepository.findByName(EUserType.ROLE_MEMBER);
            user.setUserType(userRole);
        } else {
            UserType userRole = userTypeRepository.findByName(EUserType.valueOf(strRoles));
            user.setUserType(userRole);
        }
        switch (user.getUserType().getName()){
            case ROLE_MEMBER:
                if(memberRepository.existsByMid(signUpRequest.getString("mid")))return CommonMethod.getReturnMessageError("该成员编号已被注册！");
                Member member =new Member();
                member.setName(signUpRequest.getString("name"));
                member.setMid(signUpRequest.getString("mid"));
                userRepository.save(user);
                member.setUser(user);
                memberRepository.save(member);
                break;
            case ROLE_ADMIN:
                if(adminRepository.existsByAid(signUpRequest.getString("aid")))return CommonMethod.getReturnMessageError("该管理员号已被注册！");
                if(!signUpRequest.getString("check").equals("202022300310"))return CommonMethod.getReturnMessageError("校验密码错误！");
                Admin admin =new Admin();
                admin.setName(signUpRequest.getString("name"));
                admin.setAid(signUpRequest.getString("aid"));
                userRepository.save(user);
                admin.setUser(user);
                adminRepository.save(admin);
                break;
        }


        return CommonMethod.getReturnMessageOK();
    }

    @Override
    public DataResponse changePassword(DataRequest dataRequest) {
        Integer userId= CommonMethod.getUserId();
        if(userId == null)
            return CommonMethod.getReturnMessageError("lang.comm.loginError");
        String oldPassword = dataRequest.getString("oldPassword");
        String newPassword = dataRequest.getString("newPassword");
        User u = userRepository.findById(userId).get();
        Log log=new Log(u,rightTypeRepository.getByValue(ERightType.valueOf("RIGHT_CHANGE_PASSWORD")),"修改密码");
        if(!encoder.matches(oldPassword, u.getPassword())) {
            log.setOperateState(1);
            logRepository.save(log);
            return CommonMethod.getReturnMessageError("原密码错误！");
        }
        u.setPassword(encoder.encode(newPassword));
        userRepository.save(u);
        log.setOperateState(0);
        logRepository.save(log);
        return CommonMethod.getReturnMessageOK();
    }
}
