package com.example.useraccessdivide.user.forms;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class RegisterForm {
    @NotBlank
    private String username;
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "chỉ được nhập chữ và số (độ dài 6~20).")
    private String password;
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "chỉ được nhập chữ và số (độ dài 6~20).")
    private String matchPassword;
    @Email
    private String email;
    private String firstName;
    private String lastName;
}
