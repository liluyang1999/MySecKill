package com.example.lly.dao.provider;

import com.example.lly.util.BaseUtil;

import java.sql.Timestamp;
import java.util.Map;

public class SeckillInfoSqlProvider {

    private final String seckillInfoTableName = "t_seckillinfo";

    public String queryById(Integer id) {
        return "SELECT * FROM " + seckillInfoTableName + " WHERE id = " + id;
    }

    public String queryByName(String name) {
        return "SELECT * FROM " + seckillInfoTableName + " WHERE name = " + BaseUtil.addQuotationMark(name);
    }

    public String queryAll() {
        return "SELECT * FROM " + seckillInfoTableName;
    }

    public String decreaseNumber(Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        Timestamp orderTime = (Timestamp) params.get("orderTime");
        //下单时间必须要在合规时间内，否则视为无效
        return "UPDATE " + seckillInfoTableName +
                " SET remaining_number = remaining_number - 1" +
                " WHERE id = " + id +
                " AND remaining_number > 0" +
                " AND start_time <= '" + orderTime.toString() + "'" +
                " AND end_time >= '" + orderTime.toString() + "'";
    }

    public String increaseNumber(Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        Timestamp timestamp = (Timestamp) params.get("operateTime");
        return "UPDATE" + seckillInfoTableName +
                " SET remaining_number = remaining_number + 1" +
                " WHERE id = " + id +
                " AND startTime >= '" + timestamp.toString() + "'";
    }

    public String queryNumberById(Integer id) {
        return "SELECT number FROM " + seckillInfoTableName + " WHERE id = " + id;
    }

    public String queryByLimit(Map<String, Object> params) {
        Integer index = (Integer) params.get("index");
        Integer limit = (Integer) params.get("limit");
        return "SELECT *" +
                " FROM " + seckillInfoTableName +
                " ORDER BY create_time DESC" +     //最近的单子优先
                " LIMIT " + index + " " + limit;
    }

    //Timestamp要加单引号, 在开始时间与结束时间之间
    public String queryAllSeckillInfoInProgress(Timestamp now) {
        return "SELECT * " +
                " FROM " + seckillInfoTableName +
                " WHERE start_time <= '" + now.toString() + "'" +
                " AND end_time >= '" + now.toString() + "'";
    }

    public String queryAllSeckillInfoInFuture(Timestamp now) {
        return "SELECT * " +
                " FROM " + seckillInfoTableName +
                " WHERE start_time > '" + now.toString() + "'";
    }


}
