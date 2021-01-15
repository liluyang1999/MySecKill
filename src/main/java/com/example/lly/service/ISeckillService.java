package com.example.lly.service;

import com.example.lly.entity.pojo.Product;
import com.example.lly.entity.pojo.Result;
import com.example.lly.entity.pojo.SeckillInfo;

import java.util.List;

public interface ISeckillService {

    //单个秒杀
    SeckillInfo getSeckillById(long id);

    //所有秒杀
    List<SeckillInfo> getAllSeckill();

    //得到秒杀活动售卖的所有商品
    List<Product> getAllProductInSeckill(long secKillId);

    //得到秒杀活动售卖的所有商品的数量
    int getProductNumberInSeckill(long secKillId);
    int getProductNumberInSeckill(SeckillInfo secKill);

    //判断该商品是否在秒杀商品队列里
    boolean productInSeckill(long id);
    boolean productInSeckill(Product product);

    //放入秒杀商品队列，成功返回0，失败返回-1
    boolean insertProductToSeckill(long id);
    boolean insertProductToSeckill(Product product);

    //从秒杀商品队列中移除该商品，成功返回0，失败返回-1
    boolean removeProductFromSeckill(long id);
    boolean removeProductFromSeckill(Product product);

    //上锁，普通锁 or AOP+锁
    Result lockSeckill(long userId, long seckillId);
    Result lockSecKillWithAop(long userId, long seckillId);





}
