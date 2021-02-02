package com.example.lly.dao.provider;

//Writing SQL below
public class RoleSqlProvider {

    String tableName = "t_role";

    public String queryById(Integer id) {
        return "SELECT * FROM " + tableName + " WHERE id = " + id;
    }

    public String queryAll() {
        return "SELECT * FROM " + tableName;
    }

    public String queryAllAvailableRole() {
        return "SELECT * FROM " + tableName + " WHERE available = " + 1;
    }

    public String queryAllUnavailableRole() {
        return "SELECT * FROM" + tableName + " WHERE availbale = " + 0;
    }

    public String queryByName(String name) {
        return "SELECT * FROM " + tableName + " WHERE role = " + name;
    }

}
