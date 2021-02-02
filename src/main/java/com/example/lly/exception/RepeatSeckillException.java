package com.example.lly.exception;

import com.example.lly.util.BaseUtil;

import java.io.Serial;

public class RepeatSeckillException extends BaseSeckillException {

    private static final String message = "请勿重复秒杀！";

    public RepeatSeckillException() {
        super(message);
    }

    public RepeatSeckillException(String message) {
        super(message);
    }

    public RepeatSeckillException(String message, Throwable cause) {
        super(message, cause);
    }

    public static String getMsg() {
        return message;
    }

    @Serial
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

}
