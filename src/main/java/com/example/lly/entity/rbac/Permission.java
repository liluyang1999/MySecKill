package com.example.lly.entity.rbac;

import com.example.lly.util.BaseUtil;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "t_permission")
public class Permission implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //由数据库自行修改ID
    private Integer id;

    private String name;

    @Column(columnDefinition = "enum('menu', 'button')")
    private String type;    //资源类型

    private String url;

    private String permission;

    private Integer parentId;      //父类编号

    private Boolean available;

    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "t_role_permission",
            joinColumns = {@JoinColumn(name = "permission_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles;

    @Serial
    @Transient
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}





