package com.example.lly.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//singleton, prototype, request, session, globalsession
@Scope(value = "singleton")
@Component
@Order(1)   //Bean加载的优先顺序, 越小越优先执行, 保证上锁是第一步, 同时最后结束
public aspect SeckillLockAspect {

    //利用可重入锁, 选择公平锁方案
    private static final Lock lock = new ReentrantLock(true);

    @Pointcut("@annotation(com.example.lly.aop.SeckillLock)")
    public void seckillLock() {}

    @Around("seckillLock()")
    public Object takeAround(ProceedingJoinPoint joinPoint) {
        Object object;
        lock.lock();
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            lock.unlock();
        }
        return object;
    }

}
