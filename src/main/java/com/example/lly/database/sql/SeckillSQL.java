package com.example.lly.database.sql;

import java.sql.Timestamp;
import java.util.Map;

public class SeckillSQL {

    public String querySeckillInfoById(Map<String, Object> params) {
        long seckillId = (long) params.get("seckillId");
        return "SELECT * " +
                "FROM seckill " +
                "WHERE seckill_id = " + seckillId;
    }

    public String querySeckillInfoByLimit(Map<String, Object> params) {
        int index = (int) params.get("index");
        int limit = (int) params.get("limit");
        return "SELECT * " +
                "FROM seckill" +
                "ORDER BY create_time DESC" +     //最近的单子优先
                "LIMIT " + index + " " + limit;
    }

    public String decreaseNumber(Map<String, Object> params) {
        long seckillId = (long) params.get("seckillId");
        Timestamp orderTime = (Timestamp) params.get("orderTime");
        return "UPDATE seckill" +
                "SET number = number - 1" +
                "WHERE seckillId = " + seckillId +
                "AND number > 0" +
                "AND orderTime >= start_time AND orderTime <= end_time";
    }


}
