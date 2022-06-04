package com.example.useraccessdivide.user.entities;

import lombok.*;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "permissions")
@Setter @Getter
@RequiredArgsConstructor()
@NoArgsConstructor
public class Permission extends BaseEntity implements Serializable{
	private static final long serialVersionUID = -1900437945700954619L;
	@NonNull
    private String permissionName;
    @NonNull
    private String permissionKey;
}
