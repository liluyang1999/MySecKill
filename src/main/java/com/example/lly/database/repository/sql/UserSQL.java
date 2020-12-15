package com.example.lly.database.repository.sql;

import java.util.Map;

//Writing SQL below
public class UserSQL {

    String tableName = "system_user";

    public String listAllUser() {
        return "SELECT * FROM user";
    }

    public String queryBySystemUserId(Map<String, Object> param) {
        Long userID = (Long)param.get("systemUserId");
        return "SELECT * FROM " + tableName + " WHERE id = " + userID;
    }


}
