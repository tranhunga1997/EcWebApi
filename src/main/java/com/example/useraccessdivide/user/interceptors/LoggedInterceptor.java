package com.example.useraccessdivide.user.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.common.utils.TokenProvider;

/**
 * Filter
 * 
 * @author tranh
 *
 */
@Component
public class LoggedInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// kiểm tra jwt từ request
		try {
			TokenProvider.getUsername(request);
			return true;
		} catch (MyException e) {
			response.setStatus(404);
			return false;
		}
	}
}
