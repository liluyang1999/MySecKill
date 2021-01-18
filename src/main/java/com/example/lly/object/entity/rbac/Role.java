package com.example.lly.object.entity.rbac;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "t_role")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;   //UI界面显示角色的信息
    private Boolean available = Boolean.TRUE;  //该角色是否可用

    @Transient
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_role_permission",
               joinColumns = {@JoinColumn(name = "role_id")},
               inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private List<Permission> permissions;

    @Transient
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_user_role",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> users;

}
