package com.example.lly.queue;

import com.example.lly.service.impl.SeckillServiceImpl;
import com.example.lly.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisConsumer {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SeckillServiceImpl serviceKillService;




}
