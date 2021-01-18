package com.example.lly.database.mapper;

import com.example.lly.database.sql.provider.PermissionSqlProvider;
import com.example.lly.object.entity.rbac.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {

//    @SelectProvider(type = PermissionSqlProvider.class, method = "queryByPermissionId")
//    Permission queryByPermissionId(@Param("permissionId") long id);

    @SelectProvider(type = PermissionSqlProvider.class, method = "queryByPermissionName")
    List<Permission> queryByPermissionName(@Param("name") String name);

}
