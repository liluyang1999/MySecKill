package com.example.lly.entity.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "success_seckill")
public class SuccessSecKill extends SecKill implements Serializable, Cloneable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name = "success_seckill_id")
    private long seckillId;

    @Id
    @NotNull
    @Column(name = "success_seckill_user_id")
    private long userId;

    private short state;
    private Timestamp createTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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


}

