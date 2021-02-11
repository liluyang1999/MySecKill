package com.example.lly.service.impl;

import com.example.lly.module.security.JwtTokenUtil;
import com.example.lly.service.JwtAuthService;
import com.example.lly.util.encryption.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;

@Service
public class JwtAuthServiceImpl implements JwtAuthService {

    @Resource    //@Resource按照name自动注入, @Autowired按照type注入
    private AuthenticationManager authenticationManager;

    @Resource
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthServiceImpl.class);
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;


    public String refreshLogin(String token) {
        return JwtTokenUtil.refreshToken(token);
    }

    public String requestLogin(String username, String password, Boolean rememberMe) {
        //发现错误返回空值
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null || !userDetails.getPassword().equals(MD5Util.encodeString(password))) {
            return null;
        }
        //账号和密码进行验证, 三步走, Token -> Manager -> SecurityContextHolder
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        redisTemplate.opsForHash().put("users", username, userDetails);
        return JwtTokenUtil.createToken(userDetails, rememberMe);
    }

    @Override
    public Boolean validateUsername(String token) {
        String username = JwtTokenUtil.getUsernameFromToken(token);
        UserDetails userDetails = (UserDetails) redisTemplate.opsForHash().get("users", username);
        if (userDetails == null) {
            userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails == null || !username.equals(userDetails.getUsername())) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean validateExpiration(String token) {
        return JwtTokenUtil.isExpiration(token);
    }

}
