package com.example.lly.dao.provider;

import com.example.lly.util.BaseUtil;

import java.util.Map;

public class UserSqlProvider {

    private final String userTableName = "t_user";
    private final String mediumTableName = "t_user_role";
    private final String roleTableName = "t_role";

    public String queryById(Integer id) {
        return "SELECT * FROM " + userTableName + " WHERE id = " + id;
    }

    public String queryByUsername(String username) {
        return "SELECT * FROM " + userTableName + " WHERE username = " + BaseUtil.addQuotationMark(username);
    }

    public String queryAll() {
        return "SELECT * FROM " + userTableName;
    }


    public String queryByDisplayName(String displayName) {
        return "SELECT * FROM " + userTableName + " WHERE display_name = " + BaseUtil.addQuotationMark(displayName);
    }


    public String queryByUsernameAndPassword(Map<String, Object> params) {
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        return "SELECT * FROM " + userTableName + " WHERE username = " + BaseUtil.addQuotationMark(username) +
                " AND password = " + BaseUtil.addQuotationMark(password);
    }


}
