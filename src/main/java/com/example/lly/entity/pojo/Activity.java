package com.example.lly.entity.pojo;

import com.example.lly.util.BaseUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class Activity implements Serializable, Cloneable {

    public static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

    private int ID;

    private String name;

    private String productCode;

    private int activityType;





}
