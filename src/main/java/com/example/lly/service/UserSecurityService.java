package com.example.lly.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface UserSecurityService {


    Boolean hasUsername(HttpServletRequest request, Authentication authentication);

    Boolean checkPassword(HttpServletRequest request, Authentication authentication);

    Boolean hasPermission(HttpServletRequest request, Authentication authentication);


}
