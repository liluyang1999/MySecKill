package com.example.lly.dao.provider;

import com.example.lly.entity.rbac.Role;

public class PermissionSqlProvider {

    String permissionTableName = "t_permission";
    String rolePermissionTableName = "t_role_permission";

    public String queryById(Integer id) {
        return "SELECT * FROM " + permissionTableName + " WHERE id = " + id;
    }

    public String queryByName(String name) {
        return "SELECT * FROM " + permissionTableName + " WHERE name = " + name;
    }

    public String queryByType(String type) {
        return "SELECT * FROM " + permissionTableName + " WHERE type = " + type;
    }

    public String queryAll() {
        return "SELECT * FROM " + permissionTableName;
    }

    public String queryByRole(Role role) {
        return "SELECT * FROM " + permissionTableName +
                " WHERE id IN " +
                " (SELECT permission_id FROM " + rolePermissionTableName +
                " WHERE role_id = " + role.getId() + ")";
    }

}
