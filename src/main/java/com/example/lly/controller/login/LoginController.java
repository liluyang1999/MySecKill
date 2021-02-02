package com.example.lly.controller.login;

import com.example.lly.dao.mapper.rbac.UserMapper;
import com.example.lly.dto.ResponseResult;
import com.example.lly.entity.rbac.User;
import com.example.lly.module.security.JwtTokenUtil;
import com.example.lly.service.JwtAuthService;
import com.example.lly.service.UserSecurityService;
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
        Boolean rememberMe = Boolean.valueOf(params.get("rememeberMe"));
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return new ResponseResult<>(false, "错误,账号或密码为空!");
        }

        String newToken = jwtAuthService.requestLogin(username, password, rememberMe);
        return new ResponseResult<>(true, newToken);
//        if(!userSecurityService.hasUsername(request, authentication)) {
//          return new ResponseResult<>(false, "账号不存在!");
//        } else if(!userSecurityService.hasPassword(request, authentication)) {
//            return new ResponseResult<>(false, "账号或密码出错!");
//        } else if(!userSecurityService.hasPermission(request, authentication)) {
//            return new ResponseResult<>(false, "账号无权限!");
//        }
    }


    @RequestMapping(value = "/refreshLogin", method = RequestMethod.POST)
    public ResponseResult<String> refreshLogin(@RequestHeader("${jwt.header}") String token) {
        if(StringUtils.isEmpty(token)) {
            String message = "错误, token值为空!";
            return new ResponseResult<>(false, message);
        } else if(JwtTokenUtil.isExpiration(token)) {
            String message = "错误, token令牌已失效!";
            return new ResponseResult<>(false, message);
        }
        String newToken = jwtAuthService.refreshLogin(token);
        return new ResponseResult<>(true, newToken);
    }


    //localhost:8080/login/registerUser
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public ResponseResult<User> registerUser(User user, List<String> rolesNames) {
        try {
            User userByName = userSecurityService.findUserByUsername(user.getUsername());
            if(userByName != null) {
                return new ResponseResult<>(false, "该账号已被使用!");
            }

            //给user的密码加密
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            user.setRoles(userSecurityService.findRolesByNames(rolesNames));
            userMapper.insert(user);
            return new ResponseResult<>(true, user);
        } catch (Exception e) {
            logger.error("注册用户发生错误!");
            return new ResponseResult<>(false, "用户注册发生错误!");
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

}
