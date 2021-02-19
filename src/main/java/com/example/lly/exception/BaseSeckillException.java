package com.example.lly.exception;

import com.example.lly.util.BaseUtil;

import java.io.Serial;

//Base seckill exception
public class BaseSeckillException extends RuntimeException {

    private static final String message = "秒杀异常！";
    protected int code;

    public BaseSeckillException() {
        super(message);
    }


    public BaseSeckillException(String message) {
        super(message);
    }

    public BaseSeckillException(String message, Throwable cause) {
        super(message, cause);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static String getMsg() {
        return message;
    }

    @Serial
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

}
