package com.example.lly.exception;

public class RepeatSeckillException extends BaseSeckillException {

    public RepeatSeckillException(String message) {
        super(message);
    }

    public RepeatSeckillException(String message, Throwable cause) {
        super(message, cause);
    }

}
