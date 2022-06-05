package com.example.useraccessdivide.common.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.common.utils.TokenProvider;
import com.example.useraccessdivide.user.entities.User;
import com.example.useraccessdivide.user.services.AuthenticationService;
import com.example.useraccessdivide.user.services.RefreshTokenService;

import lombok.extern.slf4j.Slf4j;

/**
 * <dd>Filter kiểm tra jwt
 * 
 * @author Mạnh Hùng
 *
 */
@Slf4j
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    AuthenticationService authService;
	@Autowired
	RefreshTokenService refreshTokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// Khởi tạo các đối tượng cần thiết
		String username = TokenProvider.getUsername(request);
		long refTokenId = TokenProvider.getRefreshTokenId(request);
		User user = authService.loadUserByUsername(username);
		// kiểm tra trạng thái khóa tài khoản
		if (!user.isEnable()) {
			throw new MyException(HttpStatus.UNAUTHORIZED, "0005", "MSG_W0004");
		}
		return true;
	}

}
