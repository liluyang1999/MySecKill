package com.example.lly.dao.provider;

import com.example.lly.entity.rbac.User;
import com.example.lly.util.BaseUtil;
import org.apache.ibatis.jdbc.SQL;

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


    public String insert(User user) {
        SQL sql = new SQL();
        sql.INSERT_INTO(userTableName);
        sql.VALUES("username", "'" + user.getUsername() + "'");
        sql.VALUES("password", "'" + user.getPassword() + "'");
        sql.VALUES("display_name", "'" + user.getDisplayName() + "'");
        sql.VALUES("enabled", user.getEnabled().toString());
        sql.VALUES("phone", "'" + user.getPhone() + "'");
        sql.VALUES("email", "'" + user.getEmail() + "'");
        return sql.toString();
    }


}
