package com.example.lly.service.impl;

import com.example.lly.dao.mapper.SeckillInfoMapper;
import com.example.lly.entity.Product;
import com.example.lly.entity.SeckillInfo;
import com.example.lly.exception.MsgResult;
import com.example.lly.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private SeckillInfoMapper seckillInfoMapper;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public SeckillInfo getSeckillInfoById(Integer id) {
        return seckillInfoMapper.queryById(id);
    }

    @Override
    public List<SeckillInfo> getAllSeckillInfo() {
        return seckillInfoMapper.queryAll();
    }

    @Override
    @Cacheable(value = "seckillInfoUrlCache", key = "#{seckillInfoId}")
    public String getSeckillInfoUrl(Integer seckillInfoId) {
        ValueOperations<String, Serializable> operation = redisTemplate.opsForValue();
        SeckillInfo seckillInfo = null;
        if(redisTemplate.hasKey(cacheName)) {
            seckillInfo = (SeckillInfo) operation.get(cacheName);
        } else {
            seckillInfo = seckillInfoMapper.queryById(seckillInfoId);  //缓存中没有，则从数据库中查找
            operation.set(cacheName, seckillInfo);   //找完同时放入缓存
        }

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime startTime = seckillInfo.getStartTime().toLocalDateTime();
        LocalDateTime endTime = seckillInfo.getEndTime().toLocalDateTime();


        return null;
    }

    @Override
    public List<Product> getAllProductInSeckillInfo(Integer secKillInfoId) {
        return null;
    }

    @Override
    public int getProductNumberInSeckill(Integer secKillId) {
        return 0;
    }

    @Override
    public int getProductNumberInSeckill(SeckillInfo secKill) {
        return 0;
    }

    @Override
    public boolean productInSeckill(Integer id) {
        return false;
    }

    @Override
    public boolean productInSeckill(Product product) {
        return false;
    }

    @Override
    public boolean insertProductToSeckill(Integer id) {
        return false;
    }

    @Override
    public boolean insertProductToSeckill(Product product) {
        return false;
    }

    @Override
    public boolean removeProductFromSeckill(Integer id) {
        return false;
    }

    @Override
    public boolean removeProductFromSeckill(Product product) {
        return false;
    }

    @Override
    public MsgResult lockSeckill(Integer userId, Integer seckillId) {
        return null;
    }

    @Override
    public MsgResult lockSecKillWithAop(Integer userId, Integer seckillId) {
        return null;
    }

    private final String cacheName = "seckillInfoCache";

}
