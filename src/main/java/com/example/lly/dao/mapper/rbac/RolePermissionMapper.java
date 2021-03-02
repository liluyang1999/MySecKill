package com.example.lly.dao.mapper.rbac;

import com.example.lly.entity.rbac.Permission;
import com.example.lly.entity.rbac.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

@Mapper
public interface RolePermissionMapper {


    @Select("SELECT * FROM t_role WHERE" +
            " id IN (SELECT role_id FROM t_role_permission WHERE permission_id = #{id})")
    Set<Role> queryAllRoleByPermissionId(Integer permissionId);


    @Select("SELECT * FROM t_permission WHERE " +
            " id IN (SELECT permission_id FROM t_role_permission WHERE role_id = #{id})")
    Set<Permission> queryAllPermissionByRoleId(Integer roleId);

}
