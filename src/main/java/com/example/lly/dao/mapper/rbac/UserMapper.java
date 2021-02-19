package com.example.lly.dao.mapper.rbac;

import com.example.lly.dao.mapper.BaseMapper;
import com.example.lly.dao.provider.UserSqlProvider;
import com.example.lly.entity.rbac.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Results(id = "userMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "displayName", column = "display_name"),
            @Result(property = "password", column = "password"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "email", column = "email"),
            @Result(property = "roles", column = "id",
                    many = @Many(select = "com.example.lly.dao.mapper.rbac.UserRoleMapper.queryAllRoleByUserId",
                            fetchType = FetchType.EAGER))
    })
    @SelectProvider(type = UserSqlProvider.class, method = "queryById")
    User queryById(@Param("id") Integer id);


    @ResultMap("userMap")
    @SelectProvider(type = UserSqlProvider.class, method = "queryByUsername")
    User queryByUsername(@Param("username") String username);


    @ResultMap("userMap")
    @SelectProvider(type = UserSqlProvider.class, method = "queryAll")
    List<User> queryAll();

    @ResultMap("userMap")
    @SelectProvider(type = UserSqlProvider.class, method = "queryByDisplayName")
    List<User> queryByDisplayName(String displayName);


    @InsertProvider(type = UserSqlProvider.class, method = "insert")
    int insert(User user);

}
