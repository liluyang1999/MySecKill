package com.example.lly.dao.provider;

public class ProductSqlProvider {

    private final String productTableName = "t_product";

    public String queryById(Integer id) {
        return "SELECT * FROM " + productTableName + " WHERE id = " + id;
    }

    public String queryByName(String name) {
        return "SELECT * FROM " + productTableName + " WHERE name = " + name;
    }

    public String queryAll() {
        return "SELECT * FROM " + productTableName;
    }
}
