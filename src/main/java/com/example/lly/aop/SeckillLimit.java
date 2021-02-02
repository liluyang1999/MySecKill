package com.example.lly.aop;

import com.example.lly.util.enumeration.SeckillLimitType;

import java.lang.annotation.*;

/**
 *流量限流, 控制器上第一道关卡, IP令桶牌注解
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface SeckillLimit {

    String key() default "";

    SeckillLimitType seckillLimitType() default SeckillLimitType.CUSTOM;

}
