package com.example.useraccessdivide.user.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Table(name = "roles")
@Getter@Setter
public class Role implements Serializable{
	private static final long serialVersionUID = 6336634263973676038L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(sequenceName = "role_seq", name = "role_seq", allocationSize = 1)
    private Long id;
	@Column(unique = true, nullable = false)
    private String roleKey;
	@Column(nullable = false)
    private String roleName;
    @Column(nullable = false)
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;

    @ManyToMany
    @JoinTable(name = "roles_permissions",
    joinColumns = {@JoinColumn(name = "role_id")},
    inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private List<Permission> permissions = new ArrayList<Permission>();
    
    @Override
    public String toString() {
    	return String.format("id= %s, roleKey= %s, roleName= %s", id, roleKey, roleName);
    }
}
