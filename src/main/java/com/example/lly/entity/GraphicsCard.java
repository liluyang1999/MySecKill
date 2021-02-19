package com.example.lly.entity;

import com.example.lly.util.BaseUtil;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "t_graphicscard")
public class GraphicsCard implements Serializable, Cloneable {

    public static final Long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Integer id;

    private Long graphicsCardId;  //商品的Id

    private int aggregateAmount;  //总金额

    private int totalNumber;  //总数量

    private int userId;  //购买人

    private int type;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;

    @Version
    private int version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getGraphicsCardId() {
        return graphicsCardId;
    }

    public void setGraphicsCardId(Long graphicsCardId) {
        this.graphicsCardId = graphicsCardId;
    }

    public int getAggregateAmount() {
        return aggregateAmount;
    }

    public void setAggregateAmount(int aggregateAmount) {
        this.aggregateAmount = aggregateAmount;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

}
