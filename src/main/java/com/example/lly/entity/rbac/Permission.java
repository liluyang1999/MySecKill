package com.example.lly.entity.rbac;

import com.example.lly.util.BaseUtil;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity   //This entity will be administered by JPA
@Table(name = "t_permission")
public class Permission implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //由数据库自行修改ID
    private Integer id;

    private String name;

    @Column(columnDefinition = "enum('menu', 'button')")
    private String resourceType;    //资源类型

    private String url;

    private String permission;

    private Integer parentId;      //父类编号

    private Boolean available = Boolean.FALSE;

    @Transient
    private List<Permission> permissions;   //不会被持久化

    @Transient
    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "t_role_permission",
               joinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    public List<Permission> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

}
