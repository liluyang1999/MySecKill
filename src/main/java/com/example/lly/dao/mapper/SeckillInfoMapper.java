package com.example.lly.dao.mapper;

import com.example.lly.dao.provider.SeckillInfoSqlProvider;
import com.example.lly.entity.SeckillInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface SeckillInfoMapper extends BaseMapper<SeckillInfo> {

    @Results(id = "seckillInfoMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "productId", column = "product_id"),
            @Result(property = "expectedNumber", column = "expected_number"),
            @Result(property = "remainingNumber", column = "remaining_number"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "version", column = "version"),
            @Result(property = "seckillPrice", column = "seckill_price"),
            @Result(property = "originalPrice", column = "original_price"),
            @Result(property = "product", column = "product_id",
                    one = @One(select = "com.example.lly.dao.mapper.ProductMapper.queryById",
                            fetchType = FetchType.EAGER))
    })
    @SelectProvider(type = SeckillInfoSqlProvider.class, method = "queryById")
    SeckillInfo queryById(@Param("id") Integer id);


    @ResultMap("seckillInfoMap")
    @SelectProvider(type = SeckillInfoSqlProvider.class, method = "queryByName")
    SeckillInfo queryByName(@Param("name") String name);


    @ResultMap("seckillInfoMap")
    @SelectProvider(type = SeckillInfoSqlProvider.class, method = "queryAll")
    List<SeckillInfo> queryAll();


    @ResultMap("seckillInfoMap")
    @SelectProvider(type = SeckillInfoSqlProvider.class, method = "queryAllSeckillInfoInProgress")
    List<SeckillInfo> queryAllSeckillInfoInProgress(Timestamp now);

    //    @ResultMap("seckillInfoMap")
    @ResultMap("seckillInfoMap")
    @SelectProvider(type = SeckillInfoSqlProvider.class, method = "queryAllSeckillInfoInFuture")
    List<SeckillInfo> queryAllSeckillInfoInFuture(Timestamp now);

    /**
     * 按照活动Id与秒杀时刻去扣减库存，下单时间要符合
     */
    @UpdateProvider(type = SeckillInfoSqlProvider.class, method = "decreaseNumber")
    int decreaseNumber(@Param("id") Integer id, @Param("orderTime") Timestamp orderTime);

    /**
     * 按照活动ID增加库存，手动设定增加的库存量
     */
    @UpdateProvider(type = SeckillInfoSqlProvider.class, method = "increaseNumber")
    int increaseNumber(@Param("seckillId") long seckillId);

    @SelectProvider(type = SeckillInfoSqlProvider.class, method = "queryNumberById")
    int queryNumberById(@Param("seckillInfoId") Integer seckillInfoId);

    /**
     * 按照索引值(从零开始)最大数量来查询，对应SQL语句的LIMIT用法
     */
    @ResultMap("seckillInfoMap")
    @SelectProvider(type = SeckillInfoSqlProvider.class, method = "queryByLimit")
    List<SeckillInfo> queryByLimit(@Param("index") int index, @Param("limit") int limit);

}
