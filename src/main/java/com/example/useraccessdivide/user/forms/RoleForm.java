package com.example.useraccessdivide.user.forms;

import lombok.Data;

import java.util.List;

import javax.validation.constraints.NotBlank;

@Data
public class RoleForm {
    @NotBlank
    private String roleName;
    
    private List<Long> permissionIds;
}
