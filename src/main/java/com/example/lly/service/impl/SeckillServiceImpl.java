package com.example.lly.service.impl;

import com.example.lly.entity.Product;
import com.example.lly.entity.SeckillInfo;
import com.example.lly.dto.Result;
import com.example.lly.service.SeckillService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Override
    public SeckillInfo getSeckillById(long id) {
        return null;
    }

    @Override
    public List<SeckillInfo> getAllSeckill() {
        return null;
    }

    @Override
    public List<Product> getAllProductInSeckill(long secKillId) {
        return null;
    }

    @Override
    public int getProductNumberInSeckill(long secKillId) {
        return 0;
    }

    @Override
    public int getProductNumberInSeckill(SeckillInfo secKill) {
        return 0;
    }

    @Override
    public boolean productInSeckill(long id) {
        return false;
    }

    @Override
    public boolean productInSeckill(Product product) {
        return false;
    }

    @Override
    public boolean insertProductToSeckill(long id) {
        return false;
    }

    @Override
    public boolean insertProductToSeckill(Product product) {
        return false;
    }

    @Override
    public boolean removeProductFromSeckill(long id) {
        return false;
    }

    @Override
    public boolean removeProductFromSeckill(Product product) {
        return false;
    }

    @Override
    public Result lockSeckill(long userId, long seckillId) {
        return null;
    }

    @Override
    public Result lockSecKillWithAop(long userId, long seckillId) {
        return null;
    }
}
