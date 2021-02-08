package com.example.lly.service;

public interface JwtAuthService {

    String requestLogin(String username, String password, Boolean rememberMe);

    String refreshLogin(String token);

    Boolean validateTokenFromHeader(String token);

}
