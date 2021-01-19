package com.example.lly.dao.provider;

import java.sql.Timestamp;
import java.util.Map;

public class SeckillInfoSqlProvider {


    public String queryByLimit(Map<String, Object> params) {
        int index = (int) params.get("index");
        int limit = (int) params.get("limit");
        return "SELECT * " +
                "FROM seckill" +
                "ORDER BY create_time DESC" +     //最近的单子优先
                "LIMIT " + index + " " + limit;
    }

    public String increaseNumber(Map<String, Object> params) {
        long seckillId = (long) params.get("seckillId");
        Timestamp timestamp = (Timestamp) params.get("operateTime");
        return "UPDATE seckill" +
                " SET number = number + 1" +
                " WHERE seckillId = " + seckillId +
                " AND startTime >= " + timestamp;
    }

    public String decreaseNumber(Map<String, Object> params) {
        long seckillId = (long) params.get("seckillId");
        Timestamp orderTime = (Timestamp) params.get("orderTime");
        //下单时间必须要在合规时间内，否则视为无效
        return "UPDATE seckill" +
                " SET number = number - 1" +
                " WHERE seckillId = " + seckillId +
                " AND number > 0" +
                " AND start_time <= " + orderTime +
                " AND end_time >= " + orderTime;
    }


}
