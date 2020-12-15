package com.example.lly.database.repository.sql;

import java.util.Map;

//Writing SQL below
public class RoleSQL {

    String tableName = "system_role";

    public String queryByRoleId(Map<String, Object> param) {
        long id = (long)param.get("id");
        return "SELECT * FROM " + tableName + "WHERE id = " + id;
    }




}
