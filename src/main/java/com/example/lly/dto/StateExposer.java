package com.example.lly.dto;

import com.example.lly.util.BaseUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateExposer implements Serializable {

    private Boolean ifOpenSeckill;  //活动是否开启

    private String encodedUrl;   //md5加密值, 校验传过来的加密Url

    private Integer seckillInfoId;    //秒杀活动连接

    private LocalDateTime now;

    private LocalDateTime start;

    private LocalDateTime end;

    @Override
    public String toString() {
        return "StateExposer{" +
                "活动状态=" + ifOpenSeckill +
                ", md5加密='" + encodedUrl + '\'' +
                ", 活动ID=" + seckillInfoId +
                ", 当前时刻=" + now +
                ", 开启时刻=" + start +
                ", 结束时刻=" + end +
                '}';
    }

    @Serial
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

}
