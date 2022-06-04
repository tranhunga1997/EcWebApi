package com.example.useraccessdivide.user.dtos;

import lombok.Data;

@Data
public class ResponseData<T> {
	private String code;
	private String message;
	private T obj;
}
