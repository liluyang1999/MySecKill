package com.example.lly.service.impl;

import com.example.lly.dao.mapper.rbac.PermissionMapper;
import com.example.lly.entity.rbac.Permission;
import com.example.lly.entity.rbac.Role;
import com.example.lly.entity.rbac.User;
import com.example.lly.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {


    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public User addPermission(User user) {
        List<Role> roleList = user.getRoles();
        for (Role role : roleList) {
            List<Permission> permissionList = new ArrayList<>(permissionMapper.queryByRole(role));
            role.setPermissions(permissionList);
        }
        return user;
    }

    @Override
    public List<User> addPermissions(List<User> userList) {
        for (User user : userList) {
            List<Role> roleList = user.getRoles();
            for (Role role : roleList) {
                List<Permission> permissionList = new ArrayList<>(permissionMapper.queryByRole(role));
                role.setPermissions(permissionList);
            }
        }
        return userList;
    }

}
