package com.example.lly.service;

import com.example.lly.dao.mapper.rbac.UserMapper;
import com.example.lly.entity.rbac.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class DemoService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;


    @CachePut(value = "user1", key = "#{user.id}")
    public void save(User user) {
        userMapper.insert(user);
    }


//    @Cacheable(value = "user10")
    public User findOne(String username) {
        if(redisTemplate.hasKey("user10")) {
            System.out.println("已经有缓存了！");
            return (User) redisTemplate.opsForValue().get("user10");
        }
        User user = userMapper.queryByUsername(username);
        redisTemplate.opsForValue().set("user10", user);
        System.out.println("做了缓存！");
        return user;
    }

    public User findAnother(String username) {
        if(!redisTemplate.hasKey("1")) {
            System.out.println("这个缓存不存在！");
            return null;
        }
        System.out.println("这个缓存在的！");
        return (User) redisTemplate.opsForValue().get("user1");
    }

}
