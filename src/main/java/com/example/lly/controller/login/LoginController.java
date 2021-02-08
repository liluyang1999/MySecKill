package com.example.lly.controller.login;

import com.example.lly.dao.mapper.rbac.UserMapper;
import com.example.lly.entity.rbac.User;
import com.example.lly.module.security.JwtTokenUtil;
import com.example.lly.service.JwtAuthService;
import com.example.lly.service.UserSecurityService;
import com.example.lly.util.result.ResponseEnum;
import com.example.lly.util.result.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/login_page")    //localhost:8080/login
public class LoginController {

    private final UserSecurityService userSecurityService;
    private final UserMapper userMapper;
    private final JwtAuthService jwtAuthService;

    @Autowired
    public LoginController(UserSecurityService userSecurityService, UserMapper userMapper, JwtAuthService jwtAuthService) {
        this.userSecurityService = userSecurityService;
        this.userMapper = userMapper;
        this.jwtAuthService = jwtAuthService;
    }


    //localhost:8080/login/requestLogin  请求验证, 返回JwtToken令牌
    @RequestMapping(value = "/requestLogin", method = RequestMethod.POST)
    public ResponseResult<String> requestLogin(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        Boolean rememberMe = Boolean.valueOf(params.get("rememberMe"));
        if (StringUtils.isEmpty(username)) {
            return ResponseResult.error(ResponseEnum.EMPTY_USERNAME);
        } else if (StringUtils.isEmpty(password)) {
            return ResponseResult.error(ResponseEnum.EMPTY_PASSWORD);
        }
        String newToken = jwtAuthService.requestLogin(username, password, rememberMe);
        if (newToken == null) {
            System.out.println("账号或密码错误");
            return ResponseResult.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        return ResponseResult.success(newToken);
    }


    //localhost:8080/login/registerUser
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public ResponseResult<?> registerUser(User user, List<String> rolesNames) {
        try {
            User userByName = userSecurityService.findUserByUsername(user.getUsername());
            if (userByName != null) {
                return ResponseResult.error(ResponseEnum.HAS_USERNAME);
            }

            //给user的密码加密
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            user.setRoles(userSecurityService.findRolesByNames(rolesNames));
            userMapper.insert(user);
            return ResponseResult.success();
        } catch (Exception e) {
            logger.error("注册用户发生错误!");
            return ResponseResult.error(ResponseEnum.SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/refreshLogin", method = RequestMethod.POST)
    public ResponseResult<String> refreshLogin(@RequestHeader("${jwt.header}") String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseResult.error(ResponseEnum.TOKEN_EMPTY);
        } else if (JwtTokenUtil.isExpiration(token)) {
            return ResponseResult.error(ResponseEnum.TOKEN_EXPIRED);
        }
        String newToken = jwtAuthService.refreshLogin(token);
        return ResponseResult.success(newToken);
    }

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

}
