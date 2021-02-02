package com.example.lly.aop;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface SeckillLock {

    String description() default "自定义注解实现秒杀限流";

}
