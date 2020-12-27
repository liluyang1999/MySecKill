package com.example.lly.database.sql;

import java.util.Map;

//Writing SQL below
public class UserSQL {

    String tableName = "system_user";

    public String listAllUser() {
        return "SELECT * FROM user";
    }

    public String queryByUserId(Map<String, Object> param) {
        Long userID = (Long)param.get("systemUserId");
        return "SELECT * FROM " + tableName + " WHERE id = " + userID;
    }

    public String queryByUserName(Map<String, Object> param) {
        String name = String.valueOf(param.get("name"));
        return "SELECT * FROM " + tableName + " WHERE name = " + name;
    }

}
