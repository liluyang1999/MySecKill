package com.example.lly.service.impl;

import com.example.lly.dao.mapper.rbac.PermissionMapper;
import com.example.lly.dao.mapper.rbac.UserMapper;
import com.example.lly.entity.rbac.Permission;
import com.example.lly.entity.rbac.Role;
import com.example.lly.entity.rbac.User;
import com.example.lly.service.UserSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserSecurityServiceImpl implements UserDetailsService, UserSecurityService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Cacheable(key = "#{username}", value = "userDetailsCache")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.queryByUsername((username));

        if(user == null) {
            throw new UsernameNotFoundException("**********该账号不存在**********");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));         //加密传输
        return user;
    }


    @Override
    public Boolean hasUsername(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Boolean hasUsername = null;

        if(principal == null) {
            logger.error("**********查询不到相关用户信息！**********");
            hasUsername = false;
        }

        if(principal instanceof User) {
            UserDetails user = (UserDetails) principal;
            String username = user.getUsername();
            if(userMapper.queryByUsername(username) == null) {
                logger.error("**********该账号不存在**********");
                hasUsername = false;
            } else {
                hasUsername = true;
            }
        }
        return hasUsername;
    }

    @Override
    public Boolean checkPassword(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Boolean isCorrect = null;

        if(principal == null) {
            logger.error("**********查询不到相关用户信息！**********");
            isCorrect = false;
        }

        if(principal instanceof User) {
            User checkedUser = (User) principal;
            String checkedPassword = checkedUser.getPassword();
            User queriedUser = userMapper.queryByUsername(checkedUser.getUsername());
            if(queriedUser == null) {
                logger.error("**********该账号不存在**********");
                isCorrect = false;
            } else if(!passwordEncoder.matches(queriedUser.getPassword(), checkedPassword)) {
                logger.error("**********密码错误，请检查！**********");
                isCorrect = false;
            } else {
                isCorrect = true;   //密码正确
            }
        }
        return isCorrect;
    }

    @Override
    public Boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Boolean hasPermission = null;

        if(principal == null) {
            logger.error("**********查询不到相关用户信息！**********");
            hasPermission = false;
        }

        if(principal instanceof UserDetails) {
            UserDetails checkedUser = (UserDetails) principal;
            String username = checkedUser.getUsername();

            HashSet<String> permissionUrls = new HashSet<>();
            User queriedUser = userMapper.queryByUsername(username);  //账号查询
            for(Role role : queriedUser.getRoles()) {
                for(Permission permission : role.getPermissions()) {
                    permissionUrls.add(permission.getUrl());  //所有permission的url统统加载出来进行匹配
                }
            }

            //查查看有无该权限，匹配成功就返回true
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            hasPermission = permissionUrls.stream().anyMatch(url -> antPathMatcher.match(url, request.getRequestURI()));
        }

        return hasPermission;
    }


    private final Logger logger = LoggerFactory.getLogger(this.getClass());


}
