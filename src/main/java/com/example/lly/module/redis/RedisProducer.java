package com.example.lly.module.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisProducer {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //将消息送入通道内
    public void sendMessageToTunnel(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }

}
