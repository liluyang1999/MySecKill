package com.example.lly.entity.RBAC;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class SystemPermission {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @Column(columnDefinition = "enum('menu', 'button')")
    private String resourceType;    //资源类型

    private String url;

    private String permission;

    private Long parentId;      //父类编号

    private Boolean available = Boolean.FALSE;

    @Transient
    private List<SystemPermission> permissions;   //不会被持久化


    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "System_Role_Permission",
               joinColumns = {@JoinColumn(name = "PermissionId")},
               inverseJoinColumns = {@JoinColumn(name = "RoleId")})
    private List<SystemRole> roles;

    public List<SystemPermission> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(List<SystemPermission> permissions) {
        this.permissions = permissions;
    }

}
