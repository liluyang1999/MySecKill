package com.example.lly.database.repository.mapper;

import com.example.lly.database.repository.sql.UserSQL;
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

    @SelectProvider(type = UserSQL.class, method = "queryBySystemUserId")
    @ResultMap("User")
    User queryBySystemUserId(@Param("systemUserId") long systemUserId);


}
