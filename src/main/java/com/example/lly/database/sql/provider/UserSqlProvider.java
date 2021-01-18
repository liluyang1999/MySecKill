package com.example.lly.database.sql.provider;

public class UserSqlProvider {

    private final String userTableName = "t_user";
    private final String mediumTableName = "t_user_role";
    private final String roleTableName = "t_role";

    public String queryById(Integer id) {
        return "SELECT * FROM " + userTableName + " WHERE id = " + id;
    }

    public String queryByName(String name) {
        return "SELECT * FROM " + userTableName + " WHERE name = " + name;
    }

    public String queryAll() {
        return "SELECT * FROM " + userTableName;
    }

}
