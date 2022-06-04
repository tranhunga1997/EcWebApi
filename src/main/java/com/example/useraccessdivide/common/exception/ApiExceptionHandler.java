package com.example.useraccessdivide.common.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.useraccessdivide.common.utils.MessageUtil;

/**
 * Class xử lý các lỗi exception
 * @author tranh
 *
 */
@RestControllerAdvice
public class ApiExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("9999", MessageUtil.getMessage("MSG_E9999")));
	}
	
	@ExceptionHandler(MyException.class)
	ResponseEntity<ErrorResponse> myException(MyException e) {
		return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getDetailCode(), e.getMessage()));
	}
	
	@ExceptionHandler(BindException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public List<String> bindExceptionHandle(BindException e) {
		// lấy danh sách thông báo lỗi 
		List<String> listErr = e.getBindingResult()
				.getAllErrors().stream()
				.map(ObjectError::getDefaultMessage)
				.collect(Collectors.toList());
		
		return listErr;
	}
}
