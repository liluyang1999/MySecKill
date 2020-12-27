package com.example.lly.entity.pojo;

import java.util.HashMap;

public class Result extends HashMap<String, Object> {

    public static final long serialVersonUID = 1L;

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
