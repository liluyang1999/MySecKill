package com.example.lly.service;

import com.example.lly.entity.rbac.User;

import java.util.List;

public interface RolePermissionService {

    User addPermission(User user);

    List<User> addPermissions(List<User> userList);

}
