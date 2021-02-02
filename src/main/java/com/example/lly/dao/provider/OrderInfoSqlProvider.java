package com.example.lly.dao.provider;

import com.example.lly.util.enumeration.When;

import java.sql.Timestamp;
import java.util.Map;

public class OrderInfoSqlProvider {

    private final String orderInfoTableName = "t_orderinfo";

    public String queryById(Map<String, Object> params) {
        Integer seckillId = (Integer) params.get("seckillId");
        Integer userId = (Integer) params.get("userId");
        return "SELECT * FROM " + orderInfoTableName +
                " WHERE seckill_id = " + seckillId +
                " AND user_id = " + userId;
    }

    public String queryAll() {
        return "SELECT * FROM " + orderInfoTableName;
    }

    public String queryBySeckillInfoId(Integer id) {
        return "SELECT * FROM " + orderInfoTableName + " WHERE seckillinfo_id = " + id;
    }

    public String queryByUserId(Integer id) {
        return "SELECT * FROM " + orderInfoTableName + " WHERE user_id = " + id;
    }

    public String queryByState(Short state) {
        return "SELECT * FROM " + orderInfoTableName + " WHERE state = " + state;
    }

    public String queryByOrderTime(Timestamp orderTime, When when) {
        switch(when) {
            case Before:
                return "SELECT * FROM " + orderInfoTableName +
                        " WHERE orderTime > " + orderTime;
            case BeforeAndOn:
                return "SELECT * FROM " + orderInfoTableName +
                        " WHERE orderTime >=" + orderTime;
            case On:
                return "SELECT * FROM " + orderInfoTableName +
                        " WHERE orderTime = " + orderTime;
            case After:
                return "SELECT * FROM " + orderInfoTableName +
                        " WHERE orderTime < " + orderTime;
            case AfterAndOn:
                return "SELECT * FROM " + orderInfoTableName +
                        " WHERE orderTime <= " + orderTime;
            default:
                return "SELECT TOP 100 * FROM " + orderInfoTableName;
        }

    }



}
