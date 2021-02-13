package com.example.lly.service.impl;

import com.example.lly.aop.SeckillLimit;
import com.example.lly.aop.SeckillLock;
import com.example.lly.dao.mapper.OrderInfoMapper;
import com.example.lly.dao.mapper.ProductMapper;
import com.example.lly.dao.mapper.SeckillInfoMapper;
import com.example.lly.dto.ExecutedResult;
import com.example.lly.dto.StateExposer;
import com.example.lly.entity.OrderInfo;
import com.example.lly.entity.Product;
import com.example.lly.entity.SeckillInfo;
import com.example.lly.exception.*;
import com.example.lly.service.SeckillService;
import com.example.lly.util.BaseUtil;
import com.example.lly.util.encryption.MD5Util;
import com.example.lly.util.enumeration.SeckillStateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private SeckillInfoMapper seckillInfoMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ProductMapper productMapper;

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
    public List<Product> getAllProduct() {
        return productMapper.queryAll();
    }

    /**
     * @param userId            该用户的id
     * @param seckillInfoId     参加的秒杀活动的id
     * @param checkedEncodedUrl 用户早前拿到的md5加密过的url，用以验证真实性
     * @return 返回秒杀处理结果，成功与否
     */
    @Override
    @SeckillLimit
    @SeckillLock
    @Transactional(rollbackOn = Exception.class)
    public ExecutedResult executeSeckillTask(Integer userId, Integer seckillInfoId, String checkedEncodedUrl) {
        //缓存中拿出加密后的Url值进行比对
        ValueOperations<String, Serializable> operation = redisTemplate.opsForValue();
        String stateExposerCacheName = BaseUtil.addIdToName(SeckillService.stateExposerGeneralCacheName, seckillInfoId);
        String trueEncodedUrl = (String) operation.get(stateExposerCacheName);
        //查询结果为空或者不匹配
        if(trueEncodedUrl != null && trueEncodedUrl.equals(checkedEncodedUrl)) {
            logger.error("*********请勿篡改秒杀！**********");
            throw new TamperSeckillException();
        }

        try {
            LocalDateTime currentTime = LocalDateTime.now();
            int reduceCount = seckillInfoMapper.decreaseNumber(seckillInfoId, Timestamp.valueOf(currentTime));
            if (reduceCount <= 0) {
                //秒杀失败
                logger.error("**********没有成功减少库存, 活动已结束, 秒杀失败！**********");
                throw new FailedSeckillException();
            } else {
                //扣减库存成功，继续检查是否属于重复秒杀
                OrderInfo orderInfo = new OrderInfo(seckillInfoId, userId, Short.valueOf("1"), Timestamp.valueOf(currentTime));
                int insertCount = orderInfoMapper.insert(orderInfo);
                if (insertCount <= 0) {
                    logger.error("*********请勿重复秒杀！**********");
                    throw new RepeatSeckillException();
                } else {
                    //秒杀成功
                    return new ExecutedResult(seckillInfoId, SeckillStateType.SUCCESS, orderInfo);
                }
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);   //可能包括AOP锁中的MyException访问过于频繁异常
            throw new BaseSeckillException();  //发生异常一定要捕获扔出去, Spring检测到RuntimeException才能回滚事务
        }
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public StateExposer getCorrespondingStateExposer(Integer seckillInfoId) {
        ValueOperations<String, Serializable> operation = redisTemplate.opsForValue();
        String seckillInfoCacheName = BaseUtil.addIdToName(seckillInfoGeneralCacheName, seckillInfoId);
        String stateExposerCacheName = BaseUtil.addIdToName(stateExposerGeneralCacheName, seckillInfoId);
        SeckillInfo seckillInfo;

        if(redisTemplate.hasKey(stateExposerCacheName)) {
            return (StateExposer) operation.get(stateExposerCacheName);
        }

        if(redisTemplate.hasKey(seckillInfoCacheName)) {
            seckillInfo = (SeckillInfo) operation.get(seckillInfoCacheName);
        } else {
            seckillInfo = seckillInfoMapper.queryById(seckillInfoId);  //缓存中没有，则从数据库中查找
            operation.set(seckillInfoCacheName, seckillInfo);   //找完同时放入缓存
        }

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime startTime = seckillInfo.getStartTime().toLocalDateTime();
        LocalDateTime endTime = seckillInfo.getEndTime().toLocalDateTime();
        if(currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
            //在秒杀时间段内，响应一个地址给客户端，同时存入缓存
            String encodedUrl = addSaltAndEncode(seckillInfoId);
            StateExposer stateExposer = new StateExposer(true, encodedUrl, seckillInfoId, currentTime, startTime, endTime);
            operation.set(stateExposerCacheName, stateExposer);
            return stateExposer;
        } else {
            StateExposer stateExposer = new StateExposer(false, null, seckillInfoId, currentTime, startTime, endTime);
            operation.set(stateExposerCacheName, stateExposer);
            return stateExposer;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(SeckillServiceImpl.class);

    @Override
    public List<SeckillInfo> getAllSeckillInfoInProgress() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return seckillInfoMapper.queryAllSeckillInfoInProgress(now);
    }

    @Override
    public List<Product> getAllProductInSeckillInfo(Integer secKillInfoId) {
        return null;
    }

    @Override
    public int getRemainingNumberInSeckill(Integer seckillInfoId) {
        return seckillInfoMapper.queryNumberById(seckillInfoId);
    }

    @Override
    public boolean productInSeckill(Integer id) {
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

    private String addSaltAndEncode(Integer seckillInfoId) {
        String randomSalt = BaseUtil.generateRandomSalt(10);
        String url = seckillInfoId + "/" + randomSalt;
        return MD5Util.encodeString(url);
    }

    @Override
    public List<SeckillInfo> getAllSeckillInfoInFuture() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return seckillInfoMapper.queryAllSeckillInfoInFuture(now);
    }

}
