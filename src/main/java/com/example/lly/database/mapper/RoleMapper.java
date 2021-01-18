package com.example.lly.database.mapper;

import com.example.lly.database.sql.provider.RoleSqlProvider;
import com.example.lly.object.entity.rbac.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {

//    @SelectProvider(type = RoleSqlProvider.class, method = "queryAllAvailableRole")
//    List<Role> queryAllAvailableRole();
//
//    @SelectProvider(type = RoleSqlProvider.class, method = "queryAllUnavailableRole")
//    List<Role> queryAllUnavailableRole();


}
