package com.example.lly.aop;

import com.example.lly.module.lock.RedissLockUtil;
import com.example.lly.module.redis.RedisComponent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Order(1)
@Aspect
@Scope
@Component
public class SeckillLockAspect {

    @Autowired
    private RedisComponent redisComponent;

    @Pointcut("@annotation(com.example.lly.aop.SeckillLock)")
    public void seckillLockAspect() {
    }

    @Around("seckillLockAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {
        System.out.println("切面锁进入");
        boolean res;
        Object proceed = null;
        String lockKey = joinPoint.getArgs()[0] + "";
        RLock rLock = RedissLockUtil.redissonClient.getFairLock(lockKey);
        try {
            res = rLock.tryLock(3, 10, TimeUnit.SECONDS);
            if (res) {
                proceed = joinPoint.proceed();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            redisComponent.putAllSeckillInfos();  //下单结束后更新缓存
            rLock.unlock();
        }
        System.out.println("切面锁结束");
        return proceed;
    }

}






