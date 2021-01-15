package com.example.lly.entity.pojo;

import com.example.lly.entity.RBAC.User;
import com.example.lly.util.BaseUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "success_seckill")
public class SuccessInfo extends SeckillInfo implements Serializable, Cloneable {

    @Serial
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

    @Id
    @NotNull
    @Column(name = "seckill_id")
    private long seckillId;     //秒杀活动的Id

    @Id
    @NotNull
    @Column(name = "seckill_user_id")
    private long userId;        //秒杀用户的Id

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    private short state;            //活动状态
    private Timestamp orderTime;    //订单创建时间，与数据库交互尽量用TimeStamp

    @Override
    public long getSeckillId() {
        return seckillId;
    }

    @Override
    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    @Override
    public Timestamp getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SuccessInfo{" +
                "orderTime=" + orderTime +
                ", seckillId=" + seckillId +
                ", userName='" + user.getName() + '\'' +
                ", phone=" + user.getPhone() +
                ", state=" + state +
                '}';
    }
}

