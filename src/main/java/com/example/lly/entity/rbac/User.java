package com.example.lly.entity.rbac;

import com.example.lly.util.BaseUtil;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//Role-Based Access Control，基于角色的访问控制
@Data
@Entity
@Table(name = "t_user")
public class User implements UserDetails, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String displayName;

    private String password;

    private Boolean enabled;

    @Column(unique = true)
    private String phone;

    @Column(unique = true)
    private String email;

    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "t_user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<Role> roles = this.getRoles();
        for(Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Serial
    @Transient
    private static final long serialVersionUID = BaseUtil.SERIAL_VERSION_UID;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", dispalyName='" + displayName + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}



