package com.example.lly.service;

import com.example.lly.dto.ExecutedResult;
import com.example.lly.dto.StateExposer;
import com.example.lly.entity.Product;
import com.example.lly.entity.SeckillInfo;
import com.example.lly.exception.MsgResult;

import java.util.List;

public interface SeckillService {

    SeckillInfo getSeckillInfoById(Integer id);

    Product getProductById(Integer id);

    List<SeckillInfo> getAllSeckillInfo();

    List<Product> getAllProduct();

    List<SeckillInfo> getAllSeckillInfoInProgress();

    List<SeckillInfo> getAllSeckillInfoInFuture();

    StateExposer getCorrespondingStateExposer(Integer seckillInfoId);

    ExecutedResult executeSeckillTask(Integer userId, Integer seckillInfo, String encodedValue);


    //得到秒杀活动售卖的所有商品
    List<Product> getAllProductInSeckillInfo(Integer secKillInfoId);

    //得到秒杀活动售卖的商品的剩余数量
    int getRemainingNumberInSeckill(Integer secKillInfoId);

    //判断该商品是否在秒杀商品队列里
    boolean productInSeckill(Integer productId);

    //放入秒杀商品队列，成功返回0，失败返回-1
    boolean insertProductToSeckill(Integer id);

    boolean insertProductToSeckill(Product product);

    //从秒杀商品队列中移除该商品，成功返回0，失败返回-1
    boolean removeProductFromSeckill(Integer id);
    boolean removeProductFromSeckill(Product product);

    //上锁，普通锁 or AOP+锁
    MsgResult lockSeckill(Integer userId, Integer seckillId);
    MsgResult lockSecKillWithAop(Integer userId, Integer seckillId);


}
