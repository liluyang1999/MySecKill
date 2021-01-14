package com.example.lly.entity.pojo;

import com.example.lly.util.BaseUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/*
@Entity 和 @Table 配套使用，优先级：@Table > @Entity
 */
@Entity
@Table(name = "seckill")  //name, schema, catalog...
public class Seckill implements Serializable, Cloneable {

    @Serial
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

    @Version
    private int version;

    @Id
    @NotNull
    @Column(name = "seckill_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seckillId;

    protected String name;

    protected int number;

    protected Activity activity;   //具体描述活动

    protected Timestamp createTime;

    protected Timestamp startTime;

    protected Timestamp endTime;

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

}


