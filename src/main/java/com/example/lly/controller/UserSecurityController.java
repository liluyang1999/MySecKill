package com.example.lly.controller;

import com.example.lly.controller.login.LoginController;
import com.example.lly.dao.mapper.rbac.UserMapper;
import com.example.lly.entity.rbac.User;
import com.example.lly.module.security.JwtTokenUtil;
import com.example.lly.service.JwtAuthService;
import com.example.lly.service.UserSecurityService;
import com.example.lly.util.encryption.MD5Util;
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
    private final UserMapper userMapper;
    private final JwtAuthService jwtAuthService;


    public UserSecurityController(UserSecurityService userSecurityService, UserMapper userMapper, JwtAuthService jwtAuthService) {
        this.userSecurityService = userSecurityService;
        this.userMapper = userMapper;
        this.jwtAuthService = jwtAuthService;
    }

    //localhost:8080/login/requestRegisterUser
    @RequestMapping(value = "/requestRegisterUser", method = RequestMethod.POST)
    public ResponseResult<?> registerUser(HttpServletRequest request) {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        request.getParameter("password");
        user.setDisplayName(request.getParameter("displayName"));
        user.setPhone(request.getParameter("phone"));
        user.setEmail(request.getParameter("email"));
        user.setEnabled(true);

        try {
            User userByName = userSecurityService.getUserByUsername(user.getUsername());
            if (userByName != null) {
                return ResponseResult.error(ResponseEnum.HAS_USERNAME);
            }
            //密码加密处理
            user.setPassword(MD5Util.secondEncode(MD5Util.encodeString(user.getPassword())));
            userMapper.insert(user);
            return ResponseResult.success();
        } catch (Exception e) {
            logger.error("注册用户发生错误!");
            return ResponseResult.error(ResponseEnum.SERVER_ERROR);
        }
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
