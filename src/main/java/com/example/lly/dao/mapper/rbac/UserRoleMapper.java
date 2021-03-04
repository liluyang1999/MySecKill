package com.example.lly.dao.mapper.rbac;

import com.example.lly.entity.rbac.Role;
import com.example.lly.entity.rbac.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

@Mapper
public interface UserRoleMapper {

    @Select("SELECT * FROM t_role WHERE" +
            " id IN (SELECT role_id FROM t_user_role WHERE user_id = #{id})")
    Set<Role> queryAllRoleByUserId(Integer userId);


    @Select("SELECT * FROM t_user WHERE" +
            " id IN (SELECT user_id FROM t_user_role WHERE role_id = #{id})")
    Set<User> queryAllUserByRoleId(Integer roleId);


    @Select("SELECT account FROM t_user WHERE" +
            " id IN (SELECT user_id FROM t_user_role WHERE role_id = #{id})")
    Set<String> queryAllUserAccountByRoleId(Integer roleId);

}
