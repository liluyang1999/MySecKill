package com.example.lly.exception;

import com.example.lly.util.BaseUtil;

import java.io.Serial;

public class TamperSeckillException extends BaseSeckillException {

    private static final String message = "检测到恶意篡改！";

    public TamperSeckillException() {
        super(message);
    }

    public TamperSeckillException(String message) {
        super(message);
    }

    public TamperSeckillException(String message, Throwable cause) {
        super(message, cause);
    }

    public static String getMsg() {
        return message;
    }

    @Serial
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

}
