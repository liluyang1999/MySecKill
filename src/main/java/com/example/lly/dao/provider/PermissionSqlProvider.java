package com.example.lly.dao.provider;

public class PermissionSqlProvider {

    String permissionTableName = "t_permission";

    public String queryById(Integer id) {
        return "SELECT * FROM " + permissionTableName + " WHERE id = " + id;
    }

    public String queryByName(String name) {
        return "SELECT * FROM " + permissionTableName + " WHERE name = " + name;
    }

    public String queryByType(String type) {
        return "SELECT * FROM " + permissionTableName + " WHERE type = " + type;
    }

    public String queryAll() {
        return "SELECT * FROM " + permissionTableName;
    }



}
