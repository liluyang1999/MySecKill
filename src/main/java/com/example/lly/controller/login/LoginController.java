package com.example.lly.controller.login;

import com.example.lly.dao.mapper.rbac.UserMapper;
import com.example.lly.module.security.JwtTokenUtil;
import com.example.lly.service.JwtAuthService;
import com.example.lly.service.UserSecurityService;
import com.example.lly.util.result.ResponseEnum;
import com.example.lly.util.result.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

@RestController
@RequestMapping("/login_page")    //localhost:8080/login
public class LoginController {

    private final UserSecurityService userSecurityService;
    private final UserMapper userMapper;
    private final JwtAuthService jwtAuthService;
    private final RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    public LoginController(UserSecurityService userSecurityService, UserMapper userMapper, JwtAuthService jwtAuthService, RedisTemplate<String, Serializable> redisTemplate) {
        this.userSecurityService = userSecurityService;
        this.userMapper = userMapper;
        this.jwtAuthService = jwtAuthService;
        this.redisTemplate = redisTemplate;
    }


    //localhost:8080/login/requestLogin  请求验证, 返回JwtToken令牌
    @RequestMapping(value = "/requestLogin", method = RequestMethod.POST)
    public ResponseResult<String> requestLogin(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        if (StringUtils.isEmpty(username)) {
            return ResponseResult.error(ResponseEnum.EMPTY_USERNAME);
        } else if (StringUtils.isEmpty(password)) {
            return ResponseResult.error(ResponseEnum.EMPTY_PASSWORD);
        }

        String newToken = jwtAuthService.requestLogin(username, password);
        if (newToken == null) {
            System.out.println("账号或密码错误");
            return ResponseResult.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        return ResponseResult.success(newToken);
    }


    //localhost:8080/login/requestLogin  请求验证, 返回JwtToken令牌
    @RequestMapping(value = "/requestLogout", method = RequestMethod.POST)
    public ResponseResult<String> requestLogout(HttpServletRequest request) {
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        String username = JwtTokenUtil.getUsernameFromToken(token);
        redisTemplate.opsForHash().delete("users", username);
        return ResponseResult.success("清空Token令牌");
    }


    @RequestMapping(value = "/refreshLogin", method = RequestMethod.POST)
    public ResponseResult<String> refreshLogin(HttpServletRequest request) {
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);

        if (StringUtils.isEmpty(token)) {
            return ResponseResult.error(ResponseEnum.TOKEN_EMPTY);
        } else if (JwtTokenUtil.isExpiration(token)) {
            return ResponseResult.error(ResponseEnum.TOKEN_EXPIRED);
        }

        String newToken = jwtAuthService.refreshLogin(token);
        System.out.println("Token令牌刷新成功");
        return ResponseResult.success(newToken);
    }


    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

}
