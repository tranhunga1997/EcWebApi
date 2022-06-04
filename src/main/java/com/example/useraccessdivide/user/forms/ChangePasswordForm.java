package com.example.useraccessdivide.user.forms;

import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class ChangePasswordForm {
	@Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "chỉ được nhập chữ và số (độ dài 6~20).")
	private String oldPwd;
	@Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "chỉ được nhập chữ và số (độ dài 6~20).")
	private String newPwd;
	@Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "chỉ được nhập chữ và số (độ dài 6~20).")
	private String newPwdConfirm;
}
