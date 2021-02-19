package com.example.lly.util.enumeration;

public enum SeckillStateType {

    SUCCESS(1, "秒杀成功"),
    FINISH(2, "秒杀结束"),
    DUPLICATE(3, "重复秒杀"),
    TAMPER(4, "篡改秒杀");

    SeckillStateType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static SeckillStateType stateOf(Integer index) {
        for(SeckillStateType state : values()) {
            if(state.getCode().equals(index)) {
                return state;
            }
        }
        return null;
    }

}
