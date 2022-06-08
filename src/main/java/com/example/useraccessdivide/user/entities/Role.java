package com.example.useraccessdivide.user.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Table(name = "roles")
@Getter@Setter
public class Role extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 6336634263973676038L;
	@Column(unique = true, nullable = false)
    private String roleKey;
    private String roleName;

    @ManyToMany
    @JoinTable(name = "roles_permissions",
    joinColumns = {@JoinColumn(name = "role_id")},
    inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private List<Permission> permissions = new ArrayList<Permission>();
    
    @Override
    public String toString() {
    	return String.format("id= %s, roleKey= %s, roleName= %s", super.getId(), roleKey, roleName);
    }
}
