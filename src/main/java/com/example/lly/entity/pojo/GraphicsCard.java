package com.example.lly.entity.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "graphics_card")
public class GraphicsCard implements Serializable {

    public static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public long id;

    private Long graphicsCardId;  //商品的Id

    private int aggregateAmount;  //总金额

    private int totalNumber;  //总数量

    private int userId;  //购买人
    
    private int type;

    private Timestamp createTime;

    @Version
    private int version;



}
