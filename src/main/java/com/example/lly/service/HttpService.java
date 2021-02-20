package com.example.lly.service;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface HttpService {

    Object get(String url, Map<String, String> headerMap, Class<?> resp);

    JSONObject post(String url, Map<String, String> headerMap, JSONObject jsonObject);

    String sendMessage(String url, HttpMethod method, MultiValueMap<String, String> params);

    String sendMessage(String url, HttpMethod method, MultiValueMap<String, String> params, String token);

}
