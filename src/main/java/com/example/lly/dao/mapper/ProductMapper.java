package com.example.lly.dao.mapper;

import com.example.lly.dao.provider.ProductSqlProvider;
import com.example.lly.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    @SelectProvider(type = ProductSqlProvider.class, method = "queryById")
    Product queryById(@Param("id") Integer id);

    @SelectProvider(type = ProductSqlProvider.class, method = "queryByName")
    Product queryByName(@Param("name") String name);

}
