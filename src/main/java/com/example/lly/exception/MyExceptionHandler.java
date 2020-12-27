package com.example.lly.exception;

import com.example.lly.entity.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义异常捕获，作用于RequestMapping上，加上rest就不用responsebody，既直接为json格式
 */
@RestControllerAdvice
public class MyExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder binder) {}

    @ExceptionHandler(MyException.class)
    public Result





}
