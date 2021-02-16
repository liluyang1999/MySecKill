package com.example.lly.module.redis;

import com.example.lly.service.SeckillService;
import com.example.lly.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisConsumer {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SeckillService seckillService;


}
