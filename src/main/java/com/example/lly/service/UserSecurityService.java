package com.example.lly.service;

import com.example.lly.entity.rbac.Role;
import com.example.lly.entity.rbac.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface UserSecurityService {

    boolean containsUserRole(Set<Role> roles);

    boolean containsAdminRole(Set<Role> roles);

    User getUserByUsername(String username);

    List<Role> getRolesByNames(List<String> name);

    List<User> getAllUsers();

    boolean insertUser(HttpServletRequest request) throws Exception;

    boolean hasPermission(User user, String permissionName);

    boolean hasRole(User user, String roleName);


}
