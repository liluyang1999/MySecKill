package com.example.lly.dao.mapper.rbac;

import com.example.lly.entity.rbac.Role;
import com.example.lly.entity.rbac.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    @Select("SELECT * FROM t_role WHERE" +
            " id IN (SELECT role_id FROM t_user_role WHERE user_id = #{id})")
    List<Role> queryAllRoleByUserId(Integer userId);


    @Select("SELECT * FROM t_user WHERE" +
            " id IN (SELECT user_id FROM t_user_role WHERE role_id = #{id})")
    List<User> queryAllUserByRoleId(Integer roleId);

    @Select("SELECT account FROM t_user WHERE" +
            " id IN (SELECT user_id FROM t_user_role WHERE role_id = #{id})")
    List<String> queryAllUserAccountByRoleId(Integer roleId);

}
