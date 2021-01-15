package com.example.lly.database.mapper;

import com.example.lly.database.sql.UserSQL;
import com.example.lly.entity.RBAC.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface UserMapper {

    @SelectProvider(type = UserSQL.class, method = "listAllUser")
    List<User> listAllUser();

    @SelectProvider(type = UserSQL.class, method = "queryByUserId")
    @ResultMap("User")
    User queryBySystemUserId(@Param("userId") long systemUserId);


}
