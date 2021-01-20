package com.example.lly.dao.mapper;

import com.example.lly.dao.provider.BaseMapperSqlProvider;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BaseMapper<T> {

    @DeleteProvider(type = BaseMapperSqlProvider.class, method = "delete")
    int delete(@Param("id") Integer id, @Param("clazz") Class<T> clazz);

    @UpdateProvider(type = BaseMapperSqlProvider.class, method = "update")
    int update(T entity);

    @InsertProvider(type = BaseMapperSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(T entity);

}

