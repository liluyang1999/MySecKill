package com.example.lly.service.impl;

import com.example.lly.dao.mapper.rbac.RoleMapper;
import com.example.lly.dao.mapper.rbac.UserMapper;
import com.example.lly.entity.rbac.Permission;
import com.example.lly.entity.rbac.Role;
import com.example.lly.entity.rbac.User;
import com.example.lly.module.redis.RedisComponent;
import com.example.lly.service.RolePermissionService;
import com.example.lly.service.UserSecurityService;
import com.example.lly.util.BaseUtil;
import com.example.lly.util.encryption.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserSecurityServiceImpl implements UserDetailsService, UserSecurityService {


    public User getUserByUsername(String username) {
        User user = (User) redisTemplate.opsForHash().get("users", username);
        if (user == null) {
            user = userMapper.queryByUsername(username);
        }
        if (user != null) {
            user.setPassword(MD5Util.secondDecode(user.getPassword()));
        }
        return user;
    }


    public boolean insertUser(HttpServletRequest request) throws Exception {
        User user = new User();
        user.setId(null);
        user.setEnabled(true);
        user.setUsername(request.getParameter("username"));
        user.setDisplayName(request.getParameter("displayName"));
        user.setPhone(request.getParameter("phone"));
        user.setEmail(request.getParameter("email"));
        String password = request.getParameter("password");
        System.out.println("password: " + password);
        user.setPassword(BaseUtil.handlePassword(MD5Util.secondEncode(password)));
        System.out.println("spassword: " + user.getPassword());
        userMapper.insert(user);   //插入失败会抛出异常, 没有返回值, 切记
        return true;
    }


    public List<Role> getRolesByNames(List<String> names) {
        List<Role> roles = new ArrayList<>();
        for (String each : names) {
            roles.add(roleMapper.queryByName(each));
        }
        return roles;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userMapper.queryByUsername((username));
        if (user == null) {
            return null;
        }
        //数据库中存储的是二次加密的密码, 需要先解密
        String decodedPassword = MD5Util.secondDecode(user.getPassword());
        user.setPassword(decodedPassword);
        return user;
    }

    public boolean containsUserRole(Set<Role> roles) {
        for (Role role : roles) {
            if (role.getRole().startsWith("user")) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAdminRole(Set<Role> roles) {
        for (Role role : roles) {
            if (role.getRole().startsWith("admin")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = redisComponent.getAllUsers();
        if (userList == null) {
            userList = userMapper.queryAll();
        }
        userList = rolePermissionService.addPermissions(userList);
        return userList;
    }

    @Override
    public boolean hasPermission(User user, String permissionName) {
        Set<Permission> permissionList;
        for (Role role : user.getRoles()) {
            permissionList = role.getPermissions();
            if (permissionList.stream().anyMatch(each -> each.getPermission().equals(permissionName))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasRole(User user, String roleName) {
        return user.getRoles().stream().anyMatch(each -> each.getRole().equals(roleName));
    }


    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionService rolePermissionService;
    private final RedisTemplate<String, Serializable> redisTemplate;
    private final RedisComponent redisComponent;

    @Autowired
    public UserSecurityServiceImpl(UserMapper userMapper, RoleMapper roleMapper, RolePermissionService rolePermissionService, RedisTemplate<String, Serializable> redisTemplate, RedisComponent redisComponent) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.rolePermissionService = rolePermissionService;
        this.redisTemplate = redisTemplate;
        this.redisComponent = redisComponent;
    }
}
