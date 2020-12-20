package com.example.lly.database.repository.mapper;

import com.example.lly.database.repository.sql.RoleSQL;
import com.example.lly.entity.RBAC.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface RoleMapper {

    @SelectProvider(type = RoleSQL.class, method = "queryByRoleID")
    @ResultMap("Role")
    Role queryByRoleID(@Param("id") Long id);

    @SelectProvider(type = RoleSQL.class, method = "queryAllAvailableRole")
    @ResultMap("Role")
    List<Role> queryAllAvailableRole();

    @SelectProvider(type = RoleSQL.class, method = "queryAllUnavailableRole")
    @ResultMap("Role")
    List<Role> queryAllUnavailableRole();


}
