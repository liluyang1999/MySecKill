package com.example.lly.object.entity;

import com.example.lly.util.BaseUtil;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 注解 Entity 和 Table 配套使用，优先级：@Table > @Entity
 */
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

    protected String name;   //秒杀活动名字

    protected Integer number;    //剩余数量

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    protected Product product;   //秒杀的商品

    protected Timestamp createTime;  //活动创建时间

    protected Timestamp startTime;   //活动开始时间

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
                ", number=" + number +
                ", productName='" + product.getName() + '\'' +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Serial
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

}


