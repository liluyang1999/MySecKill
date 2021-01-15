package com.example.lly.database.mapper;

import com.example.lly.database.sql.SeckillSQL;
import com.example.lly.entity.pojo.SeckillInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface SeckillMapper {

    /**
     * 按照秒杀活动的Id查询秒杀详情
     */
    @SelectProvider(type = SeckillSQL.class, method = "querySeckillInfoById")
    SeckillInfo querySeckillInfoById(@Param("seckillId") long seckillId);

    /**
     * 按照索引值(从零开始)最大数量来查询，对应SQL语句的LIMIT用法
     */
    @SelectProvider(type = SeckillSQL.class, method = "querySeckillInfoByLimit")
    List<SeckillInfo> querySeckillInfoByLimit(@Param("index") int index, @Param("limit") int limit);

    /**
     * 返回所有秒杀商品信息
     */
    @SelectProvider(type = SeckillSQL.class, method = "queryAllSeckillnfo")
    List<SeckillInfo> queryAllSeckillInfo();


    /**
     * 按照秒杀活动的Id与秒杀时刻去扣减库存，下单时间要符合
     */
    @SelectProvider(type = SeckillSQL.class, method = "decreaseNumber")
    int decreaseNumber(@Param("seckillId") long seckillId, @Param("orderTime") Timestamp orderTime);


}
