package com.example.lly.service;

import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

public interface HttpService {

    String sendMessageByClient(String url, MultiValueMap<String, String> params, HttpMethod method);

}
