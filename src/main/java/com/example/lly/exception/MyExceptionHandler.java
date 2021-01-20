package com.example.lly.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
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
    public MsgResult handleException(MyException exception) {
        MsgResult msgResult = new MsgResult();
        msgResult.put("code", exception.getCode());
        msgResult.put("msg", exception.getMessage());
        return msgResult;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public MsgResult handleException(DuplicateKeyException exception) {
        logger.error(exception.getMessage());
        return MsgResult.error("检测到数据库中存在着重复记录");
    }

    @ExceptionHandler(Exception.class)
    public MsgResult handleException(Exception exception) {
        logger.error(exception.getMessage());
        return MsgResult.error();
    }


}
