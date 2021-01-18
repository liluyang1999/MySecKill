package com.example.lly.database.mapper;

import com.example.lly.object.entity.rbac.Role;
import com.example.lly.object.entity.rbac.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    @Select("SELECT * FROM t_role WHERE" +
            " id IN (SELECT role_id FROM t_user_role WHERE user_id = #{id})")
    List<Role> queryAllRoleByUserId(Integer userId);


    @Select("SELECT * FROM t_user WHERE" +
            " id IN (SELECT user_id FROM t_user_role WHERE role_id = #{id})")
    List<User> queryAllUserByRoleId(Integer roleId);

}
