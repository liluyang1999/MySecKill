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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Scope
@Component
public class SeckillLimitAspect {


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

    @Pointcut("@annotation(com.example.lly.aop.SeckillLimit)") //切入点, 作用于方法上
    public void seckillLimitAspect() {
    }

    @Around("seckillLimitAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {
        System.out.println("切面令牌限制进入");
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        SeckillLimit seckillLimit = method.getAnnotation(SeckillLimit.class);
        SeckillLimitType seckillLimitType = seckillLimit.seckillLimitType();
        String key = seckillLimit.key();
        Object object;
        try {
            if (seckillLimitType == SeckillLimitType.IP) {
                //如果选择IP的话，则直接从客户端获取
                key = LocationUtil.getIpAddress();
            }
            RateLimiter rateLimiter = loadingCaches.get(key);
            Boolean flag = rateLimiter.tryAcquire();
            if (flag) {
                object = joinPoint.proceed();
            } else {
                throw new MyException("请勿访问太过频繁！");
            }
        } catch (Throwable e) {
            throw new MyException("请勿访问太频繁！");
        }
        System.out.println("切面令牌限制结束");
        return object;
    }

}

