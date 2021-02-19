package com.example.lly.dao.mapper.rbac;

import com.example.lly.dao.mapper.BaseMapper;
import com.example.lly.dao.provider.RoleSqlProvider;
import com.example.lly.entity.rbac.Role;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @SelectProvider(type = RoleSqlProvider.class, method = "queryById")
    @Results(id = "roleMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "description"),
            @Result(property = "available", column = "available"),
            @Result(property = "role", column = "role"),
            @Result(property = "users", column = "id",
                    many = @Many(select = "com.example.lly.dao.mapper.rbac.UserRoleMapper.queryAllUserAccountByRoleId",
                                 fetchType = FetchType.EAGER)),
            @Result(property = "permissions", column = "id",
                    many = @Many(select = "com.example.lly.dao.mapper.rbac.RolePermissionMapper.queryAllPermissionByRoleId",
                                 fetchType = FetchType.EAGER))
    })
    Role queryById(@Param("id") Integer id);

    @ResultType(Role.class)
    @SelectProvider(type = RoleSqlProvider.class, method = "queryAll")
    List<Role> queryAll();

    @ResultType(Role.class)
    @SelectProvider(type = RoleSqlProvider.class, method = "queryAllAvailableRole")
    List<Role> queryAllAvailableRole();

    @ResultType(Role.class)
    @SelectProvider(type = RoleSqlProvider.class, method = "queryAllUnavailableRole")
    List<Role> queryAllUnavailableRole();

    @ResultMap("roleMap")
    @SelectProvider(type = RoleSqlProvider.class, method = "queryByName")
    Role queryByName(String name);

}
