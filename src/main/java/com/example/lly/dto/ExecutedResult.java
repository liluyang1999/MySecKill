package com.example.lly.dto;

import com.example.lly.entity.OrderInfo;
import com.example.lly.util.BaseUtil;
import com.example.lly.util.enumeration.SeckillStateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutedResult implements Serializable {    //秒杀执行结果

    private long seckillInfoId;

    private SeckillStateType state;

    private String stateInfo;

    private OrderInfo orderInfo;

    //这是秒杀失败的构造函数
    public ExecutedResult(Integer seckillInfoId, SeckillStateType state) {
        this.seckillInfoId = seckillInfoId;
        this.state = state;
        this.stateInfo = state.getMsg();
        this.orderInfo = null;
    }

    //这是秒杀成功的构造函数
    public ExecutedResult(Integer seckillInfoId, SeckillStateType state, OrderInfo orderInfo) {
        this.seckillInfoId = seckillInfoId;
        this.state = state;
        this.stateInfo = state.getMsg();
        this.orderInfo = orderInfo;
    }

    @Serial
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

}
