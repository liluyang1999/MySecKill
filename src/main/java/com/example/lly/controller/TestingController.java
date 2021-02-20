package com.example.lly.controller;

import com.example.lly.dao.mapper.rbac.PermissionMapper;
import com.example.lly.dao.mapper.rbac.RoleMapper;
import com.example.lly.dao.mapper.rbac.UserMapper;
import com.example.lly.entity.rbac.Permission;
import com.example.lly.entity.rbac.Role;
import com.example.lly.entity.rbac.User;
import com.example.lly.module.lock.RedissLockDemo;
import com.example.lly.service.HttpService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
public class TestingController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RedissonClient client;

    @Autowired
    private HttpService httpService;

    @GetMapping("/queryUser")
    public User query1() {
        return userMapper.queryById(1);
    }

    @GetMapping("/queryRole")
    public Role query2() {
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return roleMapper.queryById(1);
    }

    @GetMapping("/queryPermission")
    public Permission query3() {
        return permissionMapper.queryById(11);
    }


    @GetMapping("/helloWorld")
    public void helloWorld() {
        RedissLockDemo demo = new RedissLockDemo();
        demo.testReentrantLock(client);
        demo.testFairLock(client);
//        demo.testAsyncReentrantLock(client);
//        demo.testRedLock(client, client, client);
//        demo.testMultiLock(client, client, client);
    }

    @RequestMapping("/sendCurrentTime")
    public void sendCurrentTime() throws JSONException {
        String result = httpService.sendMessage("http://localhost:8081/getCurrentTime", HttpMethod.GET, null);
        JSONObject object = new JSONObject(result);
        System.out.println(object.get("data"));
    }

}
