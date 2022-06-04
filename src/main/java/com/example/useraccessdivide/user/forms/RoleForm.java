package com.example.useraccessdivide.user.forms;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RoleForm {
    @NotBlank
    private String roleKey;
    @NotBlank
    private String roleName;
}
