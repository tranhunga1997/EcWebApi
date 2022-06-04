package com.example.useraccessdivide.common.exception;

import org.springframework.http.HttpStatus;

import com.example.useraccessdivide.common.utils.MessageUtil;

/**
 * Class exception customize
 * @author tranh
 *
 */
public class MyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int statusCode;
	private String detailCode;
	private String msgCode;
	private Object[] params;
	/**
	 * constructor
	 * @param httpStatus
	 * @param detailCode
	 * @param msgCode
	 * @param params
	 */
	public MyException(HttpStatus httpStatus, String detailCode, String msgCode, Object... params) {
		this.setStatusCode(httpStatus.value());
		this.detailCode = detailCode;
		this.msgCode = msgCode;
		this.params = params;
	}
	
	public String getDetailCode() {
		return detailCode;
	}
	
	public void setDetailCode(String detailCode) {
		this.detailCode = detailCode;
	}
	
	public String getMsgCode() {
		return msgCode;
	}
	
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public String getMessage() {
		return MessageUtil.getMessage(msgCode, params);
	}

}
