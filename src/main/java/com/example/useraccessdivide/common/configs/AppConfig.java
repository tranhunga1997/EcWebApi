package com.example.useraccessdivide.common.configs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.useraccessdivide.common.utils.EmailUtil;
import com.example.useraccessdivide.common.utils.MessageUtil;

@Configuration
public class AppConfig {
	@Autowired
	ConfigurableApplicationContext context;
	
    @Autowired
    private JavaMailSender javaMailSender;
	
	@PostConstruct
	public void setMessageUtil() {
		MessageUtil.setMessageSource(context);
	}
	
	@PostConstruct
	public void initMailService() {
		EmailUtil.setJavaMailSender(javaMailSender);
		EmailUtil.setJavaMailSender(javaMailSender);
	}
}
