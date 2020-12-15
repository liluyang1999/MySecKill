package com.example.lly.entity.RBAC;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    private String role;
    private String description;   //UI界面显示角色的信息
    private Boolean available = Boolean.FALSE;  //该角色是否可用

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission",
               joinColumns = {@JoinColumn(name = "RoleID")},
               inverseJoinColumns = {@JoinColumn(name = "PermissionID")})
    private List<Permission> permissions;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "RoleID")},
            inverseJoinColumns = {@JoinColumn(name = "UserID")})
    private List<User> users;


    private void testFunction(String str1) {

    }

}
