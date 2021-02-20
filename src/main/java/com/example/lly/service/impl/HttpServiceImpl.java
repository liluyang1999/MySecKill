package com.example.lly.service.impl;

import com.example.lly.service.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * RestTemplate是Spring用于同步client端的核心类，简化了与http服务的通信，并满足RestFul原则，程序代码可以给它提供URL，并提取结果。
 */

@Service
public class HttpServiceImpl implements HttpService {

    private final RestTemplate restTemplate;

    @Autowired
    public HttpServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Object get(String url, Map<String, String> headerMap, Class<?> resp) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);  //表单提交
        if (headerMap != null) {
            for (Map.Entry<String, String> stringEntry : headerMap.entrySet()) {
                httpHeaders.add(stringEntry.getKey(), stringEntry.getValue());
            }
        }
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<?> result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, resp);
        return result.getBody();
    }


    @Override
    public JSONObject post(String url, Map<String, String> headerMap, JSONObject jsonObject) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);  //表单提交
        if (headerMap != null) {
            for (Map.Entry<String, String> stringEntry : headerMap.entrySet()) {
                httpHeaders.add(stringEntry.getKey(), stringEntry.getValue());
            }
        }
        HttpEntity<?> httpEntity = new HttpEntity<>(jsonObject, httpHeaders);
        JSONObject result = restTemplate.postForObject(url, httpEntity, JSONObject.class);
        return result;
    }

    @Override
    public String sendMessage(String url, HttpMethod method, MultiValueMap<String, String> params) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);  //表单提交
        HttpEntity<MultiValueMap<String, String>> requestEntity;
        ResponseEntity<String> responseEntity;
        if (params != null) {
            requestEntity = new HttpEntity<>(params, httpHeaders);
            responseEntity = this.restTemplate.exchange(url, method, requestEntity, String.class, params);
        } else {
            requestEntity = new HttpEntity<>(httpHeaders);
            responseEntity = this.restTemplate.exchange(url, method, requestEntity, String.class);
        }
        //body为String类型
        return responseEntity.getBody();
    }

    @Override
    public String sendMessage(String url, HttpMethod method, MultiValueMap<String, String> params, String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);  //表单提交
        httpHeaders.add("Authorization", token);
        HttpEntity<MultiValueMap<String, String>> requestEntity;
        ResponseEntity<String> responseEntity;
        if (params != null) {
            requestEntity = new HttpEntity<>(params, httpHeaders);
            responseEntity = this.restTemplate.exchange(url, method, requestEntity, String.class, params);
        } else {
            requestEntity = new HttpEntity<>(httpHeaders);
            responseEntity = this.restTemplate.exchange(url, method, requestEntity, String.class);
        }
        //body为String类型
        return responseEntity.getBody();
    }


}
