package com.example.lly.dao.mapper;

import com.example.lly.dao.provider.OrderInfoSqlProvider;
import com.example.lly.entity.OrderInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    @Results(id = "orderInfoMap", value = {
            @Result(property = "seckillInfoId", column = "seckillinfo_id", id = true),
            @Result(property = "userId", column = "user_id", id = true),
            @Result(property = "state", column = "state"),
            @Result(property = "orderTime", column = "order_time")
    })
    @SelectProvider(type = OrderInfoSqlProvider.class, method = "queryById")
    OrderInfo queryById(@Param("seckillInfoId") Integer seckillInfoId, @Param("userId") Integer userId);

    @ResultMap("orderInfoMap")
    @SelectProvider(type = OrderInfoSqlProvider.class, method = "queryAll")
    List<OrderInfo> queryAll();

    @ResultMap("orderInfoMap")
    @SelectProvider(type = OrderInfoSqlProvider.class, method = "queryByState")
    List<OrderInfo> queryByState(Short state);

    @ResultMap("orderInfoMap")
    @SelectProvider(type = OrderInfoSqlProvider.class, method = "queryByUserId")
    List<OrderInfo> queryByUserId(Integer userId);

    @InsertProvider(type = OrderInfoSqlProvider.class, method = "insert")
    int insert(OrderInfo orderInfo);

}
