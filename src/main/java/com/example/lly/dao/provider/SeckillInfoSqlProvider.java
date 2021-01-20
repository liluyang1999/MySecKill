package com.example.lly.dao.provider;

import java.sql.Timestamp;
import java.util.Map;

public class SeckillInfoSqlProvider {

    private final String seckillTableName = "t_seckillinfo";

    public String queryById(Integer id) {
        return "SELECT * FROM " + seckillTableName + " WHERE id = " + id;
    }

    public String queryByName(String name) {
        return "SELECT * FROM " + seckillTableName + " WHERE name = " + name;
    }

    public String queryByLimit(Map<String, Object> params) {
        Integer index = (Integer) params.get("index");
        Integer limit = (Integer) params.get("limit");
        return "SELECT *" +
                " FROM " + seckillTableName +
                " ORDER BY create_time DESC" +     //最近的单子优先
                " LIMIT " + index + " " + limit;
    }

    public String increaseNumber(Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        Timestamp timestamp = (Timestamp) params.get("operateTime");
        return "UPDATE" + seckillTableName +
                " SET number = number + 1" +
                " WHERE id = " + id +
                " AND startTime >= " + timestamp;
    }

    public String decreaseNumber(Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        Timestamp orderTime = (Timestamp) params.get("orderTime");
        //下单时间必须要在合规时间内，否则视为无效
        return "UPDATE" + seckillTableName +
                " SET number = number - 1" +
                " WHERE id = " + id +
                " AND number > 0" +
                " AND start_time <= " + orderTime +
                " AND end_time >= " + orderTime;
    }


}
