package com.example.lly.service;

import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

public interface IHttpServce {

    String sendMessageByClient(String url, MultiValueMap<String, String> params, HttpMethod method);



}
