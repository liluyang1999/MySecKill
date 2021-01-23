package com.example.lly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessResult {

    private Boolean ifOpenSeckill;

    private String md5Encryption;

    private Long seckillUrl;

    private LocalDateTime now;

    private LocalDateTime start;

    private LocalDateTime end;

    @Override
    public String toString() {
        return "ProcessResult{" +
                "活动状态=" + ifOpenSeckill +
                ", md5加密='" + md5Encryption + '\'' +
                ", 活动ID=" + seckillUrl +
                ", 当前时刻=" + now +
                ", 开启时刻=" + start +
                ", 结束时刻=" + end +
                '}';
    }

}
