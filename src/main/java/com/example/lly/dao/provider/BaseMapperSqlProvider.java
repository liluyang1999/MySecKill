package com.example.lly.dao.provider;

import com.example.lly.util.BaseUtil;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.Map;

public class BaseMapperSqlProvider {


    public String queryById(Map<String, Object> params) {
        String tableName = BaseUtil.getTableName((Class<?>) (params.get("clazz")));
        Integer id = (Integer) params.get("id");
        return "SELECT * FROM " + tableName + " WHERE id = " + id;
    }


    public String queryAll(Map<String, Object> params) {
        String tableName = BaseUtil.getTableName((Class<?>) (params.get("clazz")));
        return "SELECT * FROM " + tableName;
    }


    public String insert(Object entity) {
        String tableName = BaseUtil.getTableName(entity);
        Class<?> clazz = entity.getClass();
        SQL sql = new SQL();
        sql.INSERT_INTO(tableName);

        for(Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String propertyName = field.getName();
            String columnName = field.getName().toLowerCase();
            try {
                //检查下这个entity里面这个属性有没有值
                if(field.get(entity) != null && !"".equals(field.get(entity))) {
                    sql.VALUES(columnName, "#{" + propertyName + "}");
                }
            } catch(IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sql.toString();
    }


    public String update(Object entity) {
        String tableName = BaseUtil.getTableName(entity);
        Class<?> clazz = entity.getClass();
        SQL sql = new SQL();
        sql.UPDATE(tableName);

        for(Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String propertyName = field.getName();
            String columnName = field.getName().toLowerCase();
            if(!"id".equals(propertyName)) {
                sql.SET(columnName + "=" + "#{" + propertyName + "}");
            } else {
                sql.WHERE(columnName + "=" + "#{" + propertyName + "}");
            }
        }
        return sql.toString();
    }


    public String delete(Map<String, Object> params) {
        String tableName = BaseUtil.getTableName((Class<?>)(params.get("clazz")));
        Integer id = (Integer) params.get("id");
        Class<?> clazz = params.get("clazz").getClass();
        SQL sql = new SQL();
        sql.DELETE_FROM(tableName);
        sql.WHERE("id=" + id);
        return sql.toString();
    }


}

