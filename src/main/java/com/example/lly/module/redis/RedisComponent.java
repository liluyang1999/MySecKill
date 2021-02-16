package com.example.lly.module.redis;

import com.example.lly.dao.mapper.ProductMapper;
import com.example.lly.dao.mapper.SeckillInfoMapper;
import com.example.lly.dao.mapper.rbac.UserMapper;
import com.example.lly.entity.Product;
import com.example.lly.entity.SeckillInfo;
import com.example.lly.entity.rbac.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class RedisComponent {

    private final RedisTemplate<String, Serializable> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final SeckillInfoMapper seckillInfoMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    @Autowired
    public RedisComponent(RedisTemplate<String, Serializable> redisTemplate, StringRedisTemplate stringRedisTemplate, SeckillInfoMapper seckillInfoMapper, ProductMapper productMapper, UserMapper userMapper) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.seckillInfoMapper = seckillInfoMapper;
        this.productMapper = productMapper;
        this.userMapper = userMapper;
    }

    public void putAllSeckillInfos() {
        List<SeckillInfo> seckillInfoList = seckillInfoMapper.queryAll();
        seckillInfoList.forEach(each ->
                redisTemplate.opsForHash().put("seckillInfos", each.getId(), each));
    }

    public void putAllProducts() {
        List<Product> productList = productMapper.queryAll();
        productList.forEach(each ->
                redisTemplate.opsForHash().put("products", each.getId(), each));
    }

    public void putAllUsers() {
        List<User> userList = userMapper.queryAll();
        userList.forEach(each ->
                redisTemplate.opsForHash().put("users", each.getUsername(), each));
    }

    public void putAllInAdvanced() {
        putAllSeckillInfos();
        putAllProducts();
        putAllUsers();
    }

    public List<SeckillInfo> getAllSeckillInfos() {
        List<Object> objectList = redisTemplate.opsForHash().values("seckillInfos");

        if (objectList.isEmpty()) {
            return null;
        }

        List<SeckillInfo> seckillInfoList = new ArrayList<>();
        for (Object each : objectList) {
            SeckillInfo seckillInfo = (SeckillInfo) each;
            seckillInfoList.add(seckillInfo);
        }
        return seckillInfoList;
    }

    public List<Product> getAllProducts() {
        List<Object> objectList = redisTemplate.opsForHash().values("products");

        if (objectList.isEmpty()) {
            return null;
        }

        List<Product> productList = new ArrayList<>();
        for (Object each : objectList) {
            Product product = (Product) each;
            productList.add(product);
        }
        return productList;
    }

    public List<User> getAllUsers() {
        List<Object> objectList = redisTemplate.opsForHash().values("users");

        if (objectList.isEmpty()) {
            return null;
        }

        List<User> userList = new ArrayList<>();
        for (Object each : objectList) {
            User user = (User) each;
            userList.add(user);
        }
        return userList;
    }
}
