package com.example.lly.service.impl;

import com.example.lly.service.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate是Spring用于同步client端的核心类，简化了与http服务的通信，并满足RestFul原则，程序代码可以给它提供URL，并提取结果。
 */

@Service
public class HttpServiceImpl implements HttpService {

    private RestTemplate restTemplate;

    public HttpServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String sendMessageByClient(String url, MultiValueMap<String, String> params, HttpMethod method) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);  //表单提交
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);
        //body为String类型
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(url, method, requestEntity, String.class, params);
        return responseEntity.getBody();
    }


}
