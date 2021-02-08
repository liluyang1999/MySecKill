package com.example.lly.entity;

import com.example.lly.util.BaseUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 注解 Entity 和 Table 配套使用，优先级：@Table > @Entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_seckillinfo")  //name, schema, catalog...
public class SeckillInfo implements Serializable {

    @Version
    @Column(name = "version")
    private int version;   //乐观锁版本控制

    @Id
    @NotNull
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;  //秒杀活动唯一Id

    @Column(name = "name")
    protected String name;   //秒杀活动名字

    @Column(name = "product_id")
    protected Integer productId;  //秒杀商品的id

    @Column(name = "expected_number")
    protected Integer expectedNumber;

    @Column(name = "remaining_number")
    protected Integer remainingNumber;    //剩余数量

    @Column(name = "price")
    protected Integer price;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    protected Product product;   //秒杀的商品

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    protected Timestamp createTime;  //活动创建时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_time")
    protected Timestamp startTime;   //活动开始时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_time")
    protected Timestamp endTime;     //活动结束时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "SeckillInfo{" +
                "seckillId=" + id +
                ", name='" + name + '\'' +
                ", expectedNumber=" + expectedNumber +
                ", remainingNumber=" + remainingNumber +
                ", productId='" + productId + '\'' +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Serial
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

}


