package com.example.useraccessdivide.common.utils;

import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

/**
 * Class chung thiết lập lấy thông báo từ properties
 * @author tranh
 *
 */
public class MessageUtil {

	private static MessageSource messageSource = null;
	
	/**
	 * khởi tạo message source
	 * @param context
	 */
	public static void setMessageSource(ApplicationContext context) {
		messageSource = context.getBean(MessageSource.class);
	}

	/**
	 * lấy thông tin message trong messages.properties
	 * @param msgCode mã thông báo
	 * @param params tham số truyền vào thôg báo
	 * @return thông báo
	 * @throws NoSuchMessageException không tìm thấy thông báo phù hợp với mã thông báo
	 */
	public static String getMessage(String msgCode, Object...params) throws NoSuchMessageException {
		return messageSource.getMessage(msgCode, params, Locale.ROOT);
	}

}
