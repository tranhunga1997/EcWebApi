package com.example.useraccessdivide.user.entities;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "permissions")
@Setter @Getter
@Builder
public class Permission implements Serializable{
	private static final long serialVersionUID = -1900437945700954619L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_seq")
    @SequenceGenerator(sequenceName = "permission_seq", name = "permission_seq", allocationSize = 1)
    private Long id;
	@Column(nullable = false)
    private String permissionName;
    @Column(unique = true, nullable = false)
    private String permissionKey;
    @Column(nullable = false)
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;
}
