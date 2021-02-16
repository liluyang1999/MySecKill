package com.example.lly.dao.mapper.rbac;

import com.example.lly.dao.mapper.BaseMapper;
import com.example.lly.dao.provider.PermissionSqlProvider;
import com.example.lly.entity.rbac.Permission;
import com.example.lly.entity.rbac.Role;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    @Results(id = "permissionMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "name", column = "name"),
            @Result(property = "type", column = "type"),
            @Result(property = "url", column = "url"),
            @Result(property = "permission", column = "permission"),
            @Result(property = "parentId", column = "parent_id"),
            @Result(property = "available", column = "available"),
            @Result(property = "roles", column = "id",
                    many = @Many(select = "com.example.lly.dao.mapper.rbac.RolePermissionMapper.queryAllRoleByPermissionId",
                            fetchType = FetchType.EAGER))
    })
    @SelectProvider(type = PermissionSqlProvider.class, method = "queryById")
    Permission queryById(@Param("id") Integer id);

    @ResultMap("permissionMap")
    @SelectProvider(type = PermissionSqlProvider.class, method = "queryByName")
    List<Permission> queryByName(@Param("name") String name);

    @ResultMap("permissionMap")
    @SelectProvider(type = PermissionSqlProvider.class, method = "queryByType")
    List<Permission> queryByType(@Param("type") String type);

    @ResultMap("permissionMap")
    @SelectProvider(type = PermissionSqlProvider.class, method = "queryAll")
    List<Permission> queryAll();


    @Results(value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "name", column = "name"),
            @Result(property = "type", column = "type"),
            @Result(property = "url", column = "url"),
            @Result(property = "permission", column = "permission"),
            @Result(property = "parentId", column = "parent_id"),
            @Result(property = "available", column = "available"),
    })
    @SelectProvider(type = PermissionSqlProvider.class, method = "queryByRole")
    List<Permission> queryByRole(Role role);

}
