package com.example.lly.util;

import org.apache.ibatis.jdbc.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.awt.geom.Arc2D;
import java.io.Serializable;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component("RedisUtil")
public class RedisUtil extends BaseUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    public static final String REDIS_PREFIX = "MySecKill:key:";

    private final RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    public RedisUtil(RedisTemplate<String, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 缓存value
     */
    public boolean cacheValue(String key, Serializable value, long time) {
        key = addPrefix(key, REDIS_PREFIX);
        try {
            ValueOperations<String, Serializable> valueOps = redisTemplate.opsForValue();
            valueOps.set(key, value);
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {   //Throwable is the superclass of all exceptions and errors.
            logger.error("缓存[{}]失败，value: [{}]", key, value, t);
            return false;
        }
    }


    public boolean cacheValue(String key, Serializable value, long time, TimeUnit timeUnit) {
        key = addPrefix(key, REDIS_PREFIX);
        try {
            ValueOperations<String, Serializable> valueOps = redisTemplate.opsForValue();
            valueOps.set(key, value);
            if (time > 0) redisTemplate.expire(key, time, timeUnit);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[{}]失败，Value: [{}]", key, value, t);
            return false;
        }
    }


    //default time is -1
    public boolean cacheValue(String key, Serializable value) {
        return cacheValue(key, value, -1L);
    }


    public Serializable getValue(String key) {
        key = addPrefix(key, REDIS_PREFIX);
        ValueOperations<String, Serializable> valueOps = redisTemplate.opsForValue();
        try {
            Serializable value = valueOps.get(key);
            return value;
        } catch (Throwable t) {
            logger.error("获取缓存失败，key[{}]，error[{}]", key, t);
            return null;
        }
    }


    public boolean removeKey(String key) {
        key = addPrefix(key, REDIS_PREFIX);
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Throwable t) {
            logger.error("缓存删除失败，key[{}]，error[{}]", key, t);
            return false;
        }
    }


    public boolean containsKey(String key) {
        key = addPrefix(key, REDIS_PREFIX);
        try {
            return redisTemplate.hasKey(key);
        } catch (NullPointerException e) {
            logger.error("判断缓存失败，key[{}]，error[{}]", key, e);
            return false;
        }
    }

    public long increaseValue(String key, long delta) {
        if(delta <= 0L) {
            throw new RuntimeException("递增数必须大于0");
        }
        key = addPrefix(key, REDIS_PREFIX);
        ValueOperations<String, Serializable> valueOps = redisTemplate.opsForValue();
        try {
            return valueOps.increment(key, delta);
        } catch(NullPointerException e) {
            logger.error("数据递增失败，key[{}]，error[{}]", key, e);
            return MIN_LONG;
        }
    }


    public double increaseValue(String key, double delta) {
        if(delta <= 0.0) {
            throw new RuntimeException("递增数必须大于0");
        }
        key = addPrefix(key, REDIS_PREFIX);
        ValueOperations<String, Serializable> valueOps = redisTemplate.opsForValue();
        try {
            return valueOps.increment(key, delta);
        } catch (NullPointerException e) {
            logger.error("数据递增失败，key[{}]，error[{}]", key, e);
            return MIN_DOUBLE;
        }
    }

    public long decreaseValue(String key, long delta) {
        if(delta <= 0L) {
            throw new RuntimeException("递增数必须大于0");
        }
        key = addPrefix(key, REDIS_PREFIX);
        ValueOperations<String, Serializable> valueOps = redisTemplate.opsForValue();
        try {
            return valueOps.decrement(key, delta);
        } catch(NullPointerException e) {
            logger.error("数据递增失败，key[{}]，error[{}]", key, e);
            return MIN_LONG;
        }
    }



}
