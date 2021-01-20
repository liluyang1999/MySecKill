package com.example.lly.dao.provider;

import java.util.Map;

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

    public String queryByAccount(String account) {
        return "SELECT * FROM " + userTableName + " WHERE account = " + account;
    }

    public String queryByAccountAndPassword(Map<String, Object> params) {
        String account = (String) params.get("account");
        String password = (String) params.get("password");
        return "SELECT * FROM " + userTableName + " WHERE account = " + account + "AND password = " + password;
    }

}
