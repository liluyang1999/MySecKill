package com.example.lly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//对所有返回类型的封装, 返回Json格式
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> {

    private boolean ifSuccess;  //是否顺利执行得到结果，并非秒杀成功与否

    private T content;

    private String error;

    public ResponseResult(boolean ifSuccess, T content) {
        this.ifSuccess = ifSuccess;
        this.content = content;
    }

    public ResponseResult(boolean ifSuccess, String error) {
        if (ifSuccess) {
            this.ifSuccess = true;
            this.content = (T) error;
        } else {
            this.ifSuccess = ifSuccess;
            this.error = error;
        }
    }
    @Override
    public String toString() {
        return "ResponseResult{" +
                "状态=" + this.ifSuccess +
                ", 内容=" + this.content +
                ", 错误信息='" + this.error + '\'' +
                "}";
    }

}
