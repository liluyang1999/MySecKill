package com.example.lly.exception;

import com.example.lly.util.BaseUtil;

import java.io.Serial;

//Base seckill exception
public class BaseSeckillException extends RuntimeException {

    protected int code;

    public BaseSeckillException(Throwable cause) {
        super(cause);
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

    @Serial
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

}
