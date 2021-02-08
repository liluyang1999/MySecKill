package com.example.lly.exception;

import com.example.lly.util.BaseUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;


public class MsgResult extends HashMap<String, Object> implements Serializable, Cloneable {

    @Serial
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

    public static MsgResult okay(Object msg) {
        MsgResult msgResult = new MsgResult();
        msgResult.put("code", 200);
        msgResult.put("msg", msg);
        return msgResult;
    }

    public static MsgResult error(Object msg) {
        MsgResult msgResult = new MsgResult();
        msgResult.put("code", 1000);
        msgResult.put("msg", msg);
        return msgResult;
    }

    public static MsgResult error(int code, String msg) {
        MsgResult msgResult = new MsgResult();
        msgResult.put("code", code);
        msgResult.put("msg", msg);
        return msgResult;
    }

    public static MsgResult error() {
        return error(100, "未知异常");
    }

    public static MsgResult error(String msg) {
        return error(100, msg);
    }

    public MsgResult() {
        put("code", -1);
    }

}
