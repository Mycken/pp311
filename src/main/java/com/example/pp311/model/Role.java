package com.example.pp311.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;


// Этот класс реализует интерфейс GrantedAuthority, в котором необходимо переопределить только один метод getAuthority() (возвращает имя роли).
// Имя роли должно соответствовать шаблону: «ROLE_ИМЯ», например, ROLE_USER.
//
@Entity
@Table(name = "roles")
//@Transient
public class Role implements GrantedAuthority {
    @Id
    @Column(name = "role")
    private String role;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "role"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    public Role(String role) {
           this.role = role;
    }

    public Role() {}

    @Override
    public String toString() {
        return role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
