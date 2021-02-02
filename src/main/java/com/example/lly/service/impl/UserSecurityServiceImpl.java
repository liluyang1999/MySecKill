package com.example.lly.service.impl;

import com.example.lly.dao.mapper.rbac.PermissionMapper;
import com.example.lly.dao.mapper.rbac.RoleMapper;
import com.example.lly.dao.mapper.rbac.UserMapper;
import com.example.lly.entity.rbac.Permission;
import com.example.lly.entity.rbac.Role;
import com.example.lly.entity.rbac.User;
import com.example.lly.exception.TamperSeckillException;
import com.example.lly.module.security.JwtTokenUtil;
import com.example.lly.service.SeckillService;
import com.example.lly.service.UserSecurityService;
import com.example.lly.util.BaseUtil;
import com.example.lly.util.encryption.MD5Util;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserSecurityServiceImpl implements UserDetailsService, UserSecurityService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

//    @Cacheable(key = "#{username}", value = "userDetailsCache")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("在这里loadUserByUsername了!");
        User user = userMapper.queryByUsername((username));
        if(user == null) {
            throw new UsernameNotFoundException("**********该账号不存在**********");
        }

        //数据库中存储的是二次加密的密码, 需要先解密
        String decodedPassword = MD5Util.secondDecode(user.getPassword());
        user.setPassword(decodedPassword);
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
    public Boolean hasPassword(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Boolean isCorrect = null;

        if(principal == null) {
            logger.error("**********查询不到相关用户信息！**********");
            isCorrect = false;
        }

        User checkedUser = (User) principal;
        String checkedPassword = checkedUser.getPassword();
        User queriedUser = userMapper.queryByUsername(checkedUser.getUsername());
        String queriedPassword = MD5Util.secondDecode(queriedUser.getPassword());
        if(StringUtils.compare(checkedPassword, queriedPassword) == 0) {
            logger.error("**********密码错误，请检查！**********");
            isCorrect = false;
        } else {
            isCorrect = true;   //密码正确
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

    public Boolean insertUserInfo(String username, String password, String displayName, String phone, String email) {
        String secondEncodedPass = MD5Util.secondEncode(password);
        User user = new User(username, secondEncodedPass, displayName, true, phone, email);
        return userMapper.insert(user) >= 0;
    }

    public User findUserByUsername(String username) {
        return userMapper.queryByUsername(username);
    }

    public List<Role> findRolesByNames(List<String> names) {
        List<Role> roles = new ArrayList<>();
        for(String each : names) {
            roles.add(roleMapper.queryByName(each));
        }
        return roles;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

}
