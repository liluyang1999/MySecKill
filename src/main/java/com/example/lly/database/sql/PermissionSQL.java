package com.example.lly.database.sql;

import java.util.Map;

//Write SQL below
public class PermissionSQL {

    String tableName = "system_permission";

    public String queryByPermissionId(Map<String, Object> param) {
        Long permissionId = (Long)(param.get("id"));
        return "SELECT * FROM " + tableName + " WHERE id = " + permissionId;
    }


}
