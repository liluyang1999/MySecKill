package com.example.lly.database.mapper;

import com.example.lly.database.sql.PermissionSQL;
import com.example.lly.entity.RBAC.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface PermissionMapper {

    @SelectProvider(type = PermissionSQL.class, method = "queryByPermissionId")
    @ResultMap("Permission")
    Permission queryByPermissionSQL(@Param("systemPermissionId") Long systemPermissionId);

    @SelectProvider(type = PermissionSQL.class, method = "queryByPermissionName")
    @ResultMap("Permission")
    List<Permission> queryByPermissionName(@Param("systemPermissionName") String systemPermissionName);


}
