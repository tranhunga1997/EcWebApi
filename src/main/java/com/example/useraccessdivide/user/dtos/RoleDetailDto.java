package com.example.useraccessdivide.user.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.useraccessdivide.user.entities.Permission;

public class RoleDetailDto {
	private Long id;
    private String roleKey;
    private String roleName;
    private List<Permission> permissions;
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;
    
}
