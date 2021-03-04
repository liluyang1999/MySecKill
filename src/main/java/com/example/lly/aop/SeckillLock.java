package com.example.lly.aop;

import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SeckillLock {

    String description() default "自定义注解实现AOP锁";

}
