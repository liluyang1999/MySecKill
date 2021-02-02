package com.example.lly.service;

import com.example.lly.entity.rbac.Role;
import com.example.lly.entity.rbac.User;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface UserSecurityService {

//    Boolean checkEncodedUrl(String encodedUrl, Integer SeckillInfoId);

    Boolean hasUsername(HttpServletRequest request, Authentication authentication);

    Boolean hasPermission(HttpServletRequest request, Authentication authentication);

    Boolean hasPassword(HttpServletRequest request, Authentication authentication);

    Boolean insertUserInfo(String username, String password, String displayName, String phone, String email);

    User findUserByUsername(String username);

    List<Role> findRolesByNames(List<String> name);

}
