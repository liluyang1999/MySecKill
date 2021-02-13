package com.example.lly.service;

import com.example.lly.entity.rbac.Role;
import com.example.lly.entity.rbac.User;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserSecurityService {

//    Boolean checkEncodedUrl(String encodedUrl, Integer SeckillInfoId);

    boolean hasUsername(HttpServletRequest request, Authentication authentication);

    boolean hasPermission(HttpServletRequest request, Authentication authentication);

    boolean hasPassword(HttpServletRequest request, Authentication authentication);

    boolean insertUser(HttpServletRequest request) throws Exception;

    boolean containsUserRole(List<Role> roles);

    boolean containsAdminRole(List<Role> roles);

    User getUserByUsername(String username);

    List<Role> getRolesByNames(List<String> name);

    List<User> getAllUsers();

}
