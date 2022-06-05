package com.example.useraccessdivide.user.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter@Setter
public class Role extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 6336634263973676038L;
	@Column(unique = true, nullable = false)
    private String roleKey;
    private String roleName;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "roles_permissions",
    joinColumns = {@JoinColumn(name = "role_id")},
    inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private Set<Permission> permissions = new HashSet<>();
    
    
}
