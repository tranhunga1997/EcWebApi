package com.example.useraccessdivide.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class đóng gói sử dụng cho các exception
 * @author tranh
 *
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
	private String detailCode;
	private String message;
}
