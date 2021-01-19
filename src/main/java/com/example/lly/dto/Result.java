package com.example.lly.dto;

import com.example.lly.util.BaseUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;


public class Result extends HashMap<String, Object> implements Serializable, Cloneable {

    @Serial
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

    public static Result okay(Object msg) {
        Result result = new Result();
        result.put("msg", msg);
        return result;
    }

    public static Result error(int code, String msg) {
        Result result = new Result();
        result.put("code", code);
        result.put("msg", msg);
        return result;
    }

    public static Result error(Object msg) {
        Result result = new Result();
        result.put("msg", msg);
        return result;
    }

    public static Result error() {
        return error(100, "未知异常");
    }

    public static Result error(String msg) {
        return error(100, msg);
    }

    public Result() {
        put("code", -1);
    }


}
