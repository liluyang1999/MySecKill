package com.example.lly.service.impl;

import com.example.lly.dao.mapper.rbac.PermissionMapper;
import com.example.lly.entity.rbac.Permission;
import com.example.lly.entity.rbac.Role;
import com.example.lly.entity.rbac.User;
import com.example.lly.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {


    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public User addPermission(User user) {
        Set<Role> roleList = user.getRoles();
        for (Role role : roleList) {
            Set<Permission> permissionList = new HashSet<>(permissionMapper.queryByRole(role));
            role.setPermissions(permissionList);
        }
        return user;
    }

    @Override
    public List<User> addPermissions(List<User> userList) {
        for (User user : userList) {
            Set<Role> roleList = user.getRoles();
            for (Role role : roleList) {
                Set<Permission> permissionList = new HashSet<>(permissionMapper.queryByRole(role));
                role.setPermissions(permissionList);
            }
        }
        return userList;
    }

}
