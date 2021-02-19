package com.example.lly.service;

import com.example.lly.entity.rbac.Role;
import com.example.lly.entity.rbac.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserSecurityService {

    boolean containsUserRole(List<Role> roles);

    boolean containsAdminRole(List<Role> roles);

    User getUserByUsername(String username);

    List<Role> getRolesByNames(List<String> name);

    List<User> getAllUsers();

    boolean insertUser(HttpServletRequest request) throws Exception;

    boolean hasPermission(User user, String permissionName);

    boolean hasRole(User user, String roleName);


}
