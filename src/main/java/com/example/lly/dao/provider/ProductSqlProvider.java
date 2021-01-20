package com.example.lly.dao.provider;

import org.apache.ibatis.annotations.Param;

public class ProductSqlProvider {

    private final String productTableName = "t_product";

    public String queryById(Integer id) {
        return "SELECT * FROM " + productTableName + " WHERE id = " + id;
    }

    public String queryByName(String name) {
        return "SELECT * FROM " + productTableName + " WHERE name = " + name;
    }


}
