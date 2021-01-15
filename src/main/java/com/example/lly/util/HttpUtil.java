package com.example.lly.util;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate是Spring用于同步client端的核心类，简化了与http服务的通信，并满足RestFul原则，程序代码可以给它提供URL，并提取结果。
 */

@Service
public class HttpUtil {

    public String sendMessageByClient(String url, MultiValueMap<String, String> params, HttpMethod method) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);  //表单提交
        RequestEntity<MultiValueMap<String, String>> requestEntity = new RequestEntity<MultiValueMap<String, String>>(params, httpHeaders);
        return restTemplate.exchange(url, method, requestEntity, String.class).getBody();  //通用方法发送请求
    }


}
