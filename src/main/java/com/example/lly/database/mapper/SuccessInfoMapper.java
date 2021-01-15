package com.example.lly.database.mapper;

import com.example.lly.entity.pojo.SuccessInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SuccessInfoMapper {

    /**
     * 根据Id查询订单的信息
     */
    List<SuccessInfo> querySuccessInfoById(@Param("seckillId") long seckillId, @Param("userId") long userId);

    /**
     * 查询所有订单的信息
     */
    List<SuccessInfo> queryAllSuccessInfo();

    /**
     * 插入一条订单信息，成功返回1，失败返回0
     */
    int insertSuccessInfo(@Param("seckillId") long seckillId, @Param("userId") long userId);


}
