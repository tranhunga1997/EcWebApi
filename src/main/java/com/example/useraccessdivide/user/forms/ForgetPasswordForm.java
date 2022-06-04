package com.example.useraccessdivide.user.forms;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ForgetPasswordForm {
    @Email(message = "định dạng không đúng.")
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "chỉ được nhập chữ và số (độ dài 6~20).")
    private String password;
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "chỉ được nhập chữ và số (độ dài 6~20).")
    private String matchPassword;
}
