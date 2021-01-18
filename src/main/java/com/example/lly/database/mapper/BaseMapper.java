package com.example.lly.database.mapper;

import com.example.lly.database.sql.provider.BaseMapperSqlProvider;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BaseMapper<T> {

//    @SelectProvider(type = BaseMapperSqlProvider.class, method = "queryById")
//    T queryById(@Param("id") Integer id, @Param("clazz") Class<T> clazz);
//
//    @SelectProvider(type = BaseMapperSqlProvider.class, method = "queryAll")
//    List<T> queryAll(@Param("clazz") Class<T> clazz);

    @DeleteProvider(type = BaseMapperSqlProvider.class, method = "delete")
    int delete(@Param("id") Integer id, @Param("clazz") Class<T> clazz);

    @UpdateProvider(type = BaseMapperSqlProvider.class, method = "update")
    int update(T entity);

    @InsertProvider(type = BaseMapperSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(T entity);

}
