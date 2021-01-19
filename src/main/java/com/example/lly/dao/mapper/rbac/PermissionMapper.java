package com.example.lly.dao.mapper.rbac;

import com.example.lly.dao.mapper.BaseMapper;
import com.example.lly.dao.provider.PermissionSqlProvider;
import com.example.lly.entity.rbac.Permission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {

    @Results(id = "permissionMap", value = {
            @Result(property = "id", column = "id")
    })
    @SelectProvider(type = PermissionSqlProvider.class, method = "queryByPermissionId")
    Permission queryById(@Param("permissionId") long id);


    @SelectProvider(type = PermissionSqlProvider.class, method = "queryByPermissionName")
    List<Permission> queryByName(@Param("name") String name);

}
