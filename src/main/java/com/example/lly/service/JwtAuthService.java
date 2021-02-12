package com.example.lly.service;

public interface JwtAuthService {

    String requestLogin(String username, String password);

    String refreshLogin(String token);

    Boolean validateExpiration(String token);

    Boolean validateUsername(String token);

}
