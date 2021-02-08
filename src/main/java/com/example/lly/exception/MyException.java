package com.example.lly.exception;

import com.example.lly.util.result.ResponseEnum;

public class MyException extends RuntimeException {

    private static final Long serialVersionUID = 1L;

    private Integer code = ResponseEnum.SERVER_ERROR.getCode();  //默认系统错误

    private String message;

    public MyException(String message) {
        super(message);
        this.message = message;
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public MyException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public MyException(String message, int code, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = code;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
