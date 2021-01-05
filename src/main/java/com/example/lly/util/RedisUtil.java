package com.example.lly.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("RedisUtil")
public class RedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    //前缀符号
    public static final String PREFIX_VALUE = "seckill:value";

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 缓存value
     */
    public boolean cacheValue(String key, Object value, long time) {
        key = BaseUtil.addPrefix(key, PREFIX_VALUE);
        try {
            ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
            valueOps.set(key, value);
            if(time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {   //Throwable is the superclass of all exceptions and errors.
            logger.error("缓存[{}]失败，value: [{}]", key, value, t);
            return false;
        }
    }


    public boolean cacheValue(String key, Object value, long time, TimeUnit timeUnit) {
        key = BaseUtil.addPrefix(key, PREFIX_VALUE);
        try {
            ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
            valueOps.set(key, value);
            if(time > 0) redisTemplate.expire(key, time, timeUnit);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[{}]失败，value: [{}]", key, value, t);
            return false;
        }
    }

    //default time is -1
    public boolean cacheValue(String key, Object value) {
        return cacheValue(key, value, -1L);
    }

    public boolean containsValue(String key) {
        key = BaseUtil.addPrefix(key, PREFIX_VALUE);
        try {
            return redisTemplate.hasKey(key);
        } catch(NullPointerException e) {
            logger.error("判断缓存失败，key[{}]，error[" + e + "]", key);
            return false;
        }
    }

//    public boolean





}
