package com.example.lly.service.impl;

import com.example.lly.module.security.JwtTokenUtil;
import com.example.lly.service.JwtAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JwtAuthServiceImpl implements JwtAuthService {

    @Resource    //@Resource按照name自动注入, @Autowired按照type注入
    private AuthenticationManager authenticationManager;

    @Resource
    private UserDetailsService userDetailsService;

    public String requestLogin(String username, String password, Boolean rememberMe) {
        //账号和密码进行验证, 三步走, Token -> Manager -> SecurityContextHolder
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return JwtTokenUtil.createToken(userDetails, rememberMe);
    }


    public String refreshLogin(String token) {
        return JwtTokenUtil.refreshToken(token);
    }



}
