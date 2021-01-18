package com.example.lly.database.sql.provider;

import java.util.Map;

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
