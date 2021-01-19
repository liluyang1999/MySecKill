package com.example.lly.dao.provider;

//Writing SQL below
public class RoleSqlProvider {

    String tableName = "t_role";

    public String queryAllAvailableRole() {
        return "SELECT * FROM " + tableName + " WHERE available = " + 1;
    }

    public String queryAllUnavailableRole() {
        return "SELECT * FROM" + tableName + " WHERE availbale = " + 0;
    }



}
