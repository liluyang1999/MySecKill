package com.example.lly.util.result;

import lombok.Data;

//对所有返回类型的封装, 返回Json格式
@Data
public class ResponseResult<T> {

    private Integer code;   //状态码

    private String msg;   //提示信息

    private T data;        //内容

    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(ResponseEnum.SUCCESS.getCode());
        result.setMsg(ResponseEnum.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> ResponseResult<T> success() {
        return success(null);
    }

    public static <T> ResponseResult<T> error(ResponseEnum responseData) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(responseData.getCode());
        result.setMsg(responseData.getMsg());
        result.setData(null);
        return result;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "状态=" + this.code +
                ", 信息='" + this.msg + '\'' +
                ", 内容=" + this.data +
                "}";
    }

}
