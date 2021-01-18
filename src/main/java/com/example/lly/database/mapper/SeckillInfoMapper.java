package com.example.lly.database.mapper;

import com.example.lly.database.sql.provider.SeckillInfoSqlProvider;
import com.example.lly.object.entity.SeckillInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.sql.Timestamp;
import java.util.List;

public interface SeckillInfoMapper extends BaseMapper<SeckillInfo> {


    /**
     * 按照索引值(从零开始)最大数量来查询，对应SQL语句的LIMIT用法
     */
    @SelectProvider(type = SeckillInfoSqlProvider.class, method = "queryByLimit")
    List<SeckillInfo> queryByLimit(@Param("index") int index, @Param("limit") int limit);


    /**
     * 按照活动ID增加库存，手动设定增加的库存量
     */
    @SelectProvider(type = SeckillInfoSqlProvider.class, method = "increaseNumber")
    int increaseNumber(@Param("seckillId") long seckillId);

    /**
     * 按照活动Id与秒杀时刻去扣减库存，下单时间要符合
     */
    @SelectProvider(type = SeckillInfoSqlProvider.class, method = "decreaseNumber")
    int decreaseNumber(@Param("seckillId") long seckillId, @Param("orderTime") Timestamp orderTime);


}
