package com.example.lly.dao.mapper.rbac;

import com.example.lly.entity.rbac.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RolePermissionMapper {


    @Select("SELECT * FROM t_role")
    List<Role> queryAllRoleByPermissionId(Integer permissionId);





}
