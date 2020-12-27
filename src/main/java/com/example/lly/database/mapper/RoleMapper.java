package com.example.lly.database.mapper;

import com.example.lly.database.sql.RoleSQL;
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
    Role queryByRoleID(@Param("systemRoleId") Long systemRoleId);

    @SelectProvider(type = RoleSQL.class, method = "queryAllAvailableRole")
    @ResultMap("Role")
    List<Role> queryAllAvailableRole();

    @SelectProvider(type = RoleSQL.class, method = "queryAllUnavailableRole")
    @ResultMap("Role")
    List<Role> queryAllUnavailableRole();


}
