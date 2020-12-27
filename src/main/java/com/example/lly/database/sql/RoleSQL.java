package com.example.lly.database.sql;

import java.util.Map;

//Writing SQL below
public class RoleSQL {

    String tableName = "system_role";

    public String queryByRoleId(Map<String, Object> param) {
        long id = (long)param.get("id");
        return "SELECT * FROM " + tableName + " WHERE id = " + id;
    }

    public String queryAllAvailableRole() {
        return "SELECT * FROM " + tableName + " WHERE available = " + 1;
    }

    public String queryAllUnavailableRole() {
        return "SELECT * FROM" + tableName + " WHERE availbale = " + 0;
    }



}
