package com.example.useraccessdivide.user.forms;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class LoginForm implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2133162953249749661L;
	@NotBlank(message = "Hãy nhập tài khoản.")
	private String username;
	@NotBlank(message = "Hãy nhập mật khẩu.")
	@Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "Mật khẩu chỉ được nhập chữ và số (độ dài 6~20).")
    private String password;
}
