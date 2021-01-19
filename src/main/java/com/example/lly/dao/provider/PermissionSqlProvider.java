package com.example.lly.dao.provider;

import java.util.Map;

//Write SQL below
public class PermissionSqlProvider {

    String tableName = "t_permission";

//    public String queryByPermissionId(Map<String, Object> param) {
//        Long permissionId = (Long)(param.get("id"));
//        return "SELECT * FROM " + tableName + " WHERE id = " + permissionId;
//    }

    public String queryByPermissionName(Map<String, Object> params) {
        String name = (String) params.get("name");
        return "SELECT *" +
                " FROM " + tableName +
                " WHERE name=" + name;
    }


}
