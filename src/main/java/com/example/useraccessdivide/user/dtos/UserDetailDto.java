package com.example.useraccessdivide.user.dtos;

import java.time.LocalDateTime;

import com.example.useraccessdivide.user.entities.Role;

import lombok.Data;

@Data
public class UserDetailDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enable;
    private Role role;
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;
}
