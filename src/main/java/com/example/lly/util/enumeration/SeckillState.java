package com.example.lly.util.enumeration;

public enum SeckillState {

    FINISH(0, "秒杀结束"), SUCCESS(1, "秒杀成功"),  FAIL(2, "秒杀失败");

    SeckillState(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static SeckillState stateOf(Integer index) {
        for(SeckillState state : values()) {
            if(state.getCode() == index) {
                return state;
            }
        }
        return null;
    }

}
