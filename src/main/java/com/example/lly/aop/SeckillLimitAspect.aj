package com.example.lly.aop;

import com.example.lly.exception.MyException;
import com.example.lly.util.LocationUtil;
import com.example.lly.util.enumeration.SeckillLimitType;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Aspect
@Configuration
public aspect SeckillLimitAspect {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    //每个IP分配相应的令牌桶，每天定时清空刷新
    private static final LoadingCache<String, RateLimiter> loadingCaches = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.DAYS)  //有效期为一天
            .expireAfterWrite(10, TimeUnit.DAYS)
            .maximumSize(1500)
            .build(new CacheLoader<>() {
                @Override
                public RateLimiter load(String key) {
                    return RateLimiter.create(10);
                }
            });


    @Pointcut(value = "@annotation(com.example.lly.aop.SeckillLimit)") //切入点, 作用于方法上
    public void SeckillServiceAspect() {}


    @Around(value = "SeckillServiceAspect()")
    public Object takeAround(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        SeckillLimit seckillLimit = method.getAnnotation(SeckillLimit.class);
        SeckillLimitType seckillLimitType = seckillLimit.seckillLimitType();
        String key = seckillLimit.key();
        Object object;
        try {
            if(seckillLimitType == SeckillLimitType.IP) {
                //如果选择IP的话，则直接从客户端获取
                key = LocationUtil.getIpAddress();
            }
            RateLimiter rateLimiter = loadingCaches.get(key);
            Boolean flag = rateLimiter.tryAcquire();
            if(flag) {
                object = joinPoint.proceed();
            } else {
                throw new MyException("请勿访问太过频繁！");
            }
        } catch (Throwable e) {
            throw new MyException("请勿访问太频繁！");
        }
        return object;
    }

    private static final Logger logger = LoggerFactory.getLogger(SeckillLimitAspect.class);

}
