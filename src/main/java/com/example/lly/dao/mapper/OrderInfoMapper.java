package com.example.lly.dao.mapper;

import com.example.lly.dao.provider.OrderInfoSqlProvider;
import com.example.lly.entity.OrderInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    @Results(id = "OrderInfoMap", value = {
            @Result(property = "seckillInfoId", column = "seckillinfo_id", id = true),
            @Result(property = "userId", column = "user_id", id = true),
            @Result(property = "state", column = "state"),
            @Result(property = "orderTime", column = "order_time")
    })
    @SelectProvider(type = OrderInfoSqlProvider.class, method = "queryById")
    OrderInfo queryById(@Param("seckillInfoId") Integer seckillInfoId, @Param("userId") Integer userId);

    @ResultType(OrderInfo.class)
    @SelectProvider(type = OrderInfoSqlProvider.class, method = "queryAll")
    List<OrderInfo> queryAll();

    @ResultType(OrderInfo.class)
    @SelectProvider(type = OrderInfoSqlProvider.class, method = "queryByUserId")
    List<OrderInfo> queryByUserId();

    @InsertProvider(type = OrderInfoSqlProvider.class, method = "insert")
    int insert(OrderInfo orderInfo);

}
