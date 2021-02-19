package com.example.lly.service;

import com.example.lly.dto.ExecutedResult;
import com.example.lly.dto.StateExposer;
import com.example.lly.entity.Product;
import com.example.lly.entity.SeckillInfo;
import com.example.lly.entity.rbac.User;

import java.util.List;

public interface SeckillService {

    SeckillInfo getSeckillInfoById(Integer id);

    Product getProductById(Integer id);

    List<SeckillInfo> getAllSeckillInfo();

    List<Product> getAllProduct();

    List<SeckillInfo> getAllSeckillInfoInProgress();

    List<SeckillInfo> getAllSeckillInfoInFuture();

    StateExposer getCorrespondingStateExposer(Integer seckillInfoId, User user);

    ExecutedResult executeSeckillTask(String username, Integer seckillInfo, String encodedValue);

    boolean hasOrderBefore(Integer seckillInfoId, Integer userId);

}
