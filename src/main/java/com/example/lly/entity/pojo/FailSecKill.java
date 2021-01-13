package com.example.lly.entity.pojo;

import com.example.lly.util.BaseUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "fail_seckill")
public class FailSecKill extends SecKill implements Serializable, Cloneable {

    @Serial
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

    @Id
    @NotNull
    @Column(name = "success_seckill_id")
    private long seckillId;

    @Id
    @NotNull
    @Column(name = "fail_seckill_id")
    private long userId;





}
