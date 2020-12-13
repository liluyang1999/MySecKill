package com.example.lly.entity.RBAC;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class SystemRole {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    private String role;
    private String description;   //UI界面显示角色的信息
    private Boolean available = Boolean.FALSE;  //该角色是否可用

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "system_role_permission",
               joinColumns = {@JoinColumn(name = "RoleID")},
               inverseJoinColumns = {@JoinColumn(name = "PermissionID")})
    private List<SystemPermission> permissions;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "system_user_role",
            joinColumns = {@JoinColumn(name = "RoleID")},
            inverseJoinColumns = {@JoinColumn(name = "UserID")})
    private List<SystemUser> systemUsers;


    private void testFunction(String str1) {

    }

}
