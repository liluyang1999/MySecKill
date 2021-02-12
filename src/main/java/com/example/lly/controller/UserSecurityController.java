package com.example.lly.controller;

import com.example.lly.controller.login.LoginController;
import com.example.lly.entity.rbac.User;
import com.example.lly.module.security.JwtTokenUtil;
import com.example.lly.service.JwtAuthService;
import com.example.lly.service.UserSecurityService;
import com.example.lly.util.result.ResponseEnum;
import com.example.lly.util.result.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserSecurityController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final UserSecurityService userSecurityService;
    private final JwtAuthService jwtAuthService;


    public UserSecurityController(UserSecurityService userSecurityService, JwtAuthService jwtAuthService) {
        this.userSecurityService = userSecurityService;
        this.jwtAuthService = jwtAuthService;
    }

    //localhost:8080/login/requestRegisterUser
    @RequestMapping(value = "/requestRegisterUser", method = RequestMethod.POST)
    public ResponseResult<?> registerUser(HttpServletRequest request) {
        System.out.println(request.getHeader(JwtTokenUtil.TOKEN_HEADER));
        if (request.getParameter("username") == null) {
            return ResponseResult.error(ResponseEnum.FAILED);
        }

        try {
            userSecurityService.insertUser(request);
        } catch (Exception e) {
            return ResponseResult.error(ResponseEnum.INSERT_ERROR);
        }

        return ResponseResult.success();
    }


    @RequestMapping(value = "/requestUserInfo", method = RequestMethod.POST)
    public ResponseResult<?> requestUserInfo(HttpServletRequest request) {
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        if (jwtAuthService.validateExpiration(token)) {
            return ResponseResult.error(ResponseEnum.TOKEN_EXPIRED);
        }

        if (jwtAuthService.validateUsername(token)) {
            return ResponseResult.error(ResponseEnum.UNMATCHED_USERNAME);
        }

        User user = userSecurityService.getUserByUsername(JwtTokenUtil.getUsernameFromToken(token));
        return ResponseResult.success(user);
    }

}
