package com.example.lly.database.mapper;

import com.example.lly.entity.pojo.SeckillInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface SeckillMapper {

    /**
     * 按照秒杀活动的Id查询秒杀详情
     */
    SeckillInfo querySeckillInfoById(@Param("seckillId") long seckillId);

    /**
     * 按照limit个数查询数量
     */
    List<SeckillInfo> querySeckillInfoByLimit(@Param("limit") int limit);

    /**
     * 返回所有秒杀商品信息
     */
    List<SeckillInfo> queryAllSeckillInfo();


    /**
     * 按照秒杀活动的Id与秒杀时刻去扣减库存
     */
    int decreaseNumber(@Param("seckillId") long seckillId, @Param("preciseTime") Timestamp preciseTime);



}
