package com.example.lly.dao.mapper;

import com.example.lly.dao.provider.ProductSqlProvider;
import com.example.lly.entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    @Results(id = "productMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "name", column = "name"),
            @Result(property = "price", column = "price"),
            @Result(property = "filePath", column = "file_path")
    })
    @SelectProvider(type = ProductSqlProvider.class, method = "queryById")
    Product queryById(@Param("id") Integer id);

    @ResultMap("productMap")
    @SelectProvider(type = ProductSqlProvider.class, method = "queryByName")
    Product queryByName(@Param("name") String name);

    @ResultMap("productMap")
    @SelectProvider(type = ProductSqlProvider.class, method = "queryAll")
    List<Product> queryAll();

}
