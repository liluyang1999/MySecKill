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
    Role queryByRoleID(@Param("roleId") Long roleId);

    @SelectProvider(type = RoleSQL.class, method = "queryAllAvailableRole")
    List<Role> queryAllAvailableRole();

    @SelectProvider(type = RoleSQL.class, method = "queryAllUnavailableRole")
    List<Role> queryAllUnavailableRole();


}
