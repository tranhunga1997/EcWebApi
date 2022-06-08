package com.example.useraccessdivide.user.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RoleDto {
	private Long id;
    private String roleKey;
    private String roleName;
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;
}
