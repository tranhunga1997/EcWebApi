package com.example.useraccessdivide.common.utils;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Class chung liên quan các thao tác về mail
 * @author tranh
 *
 */
public class EmailUtil {
    private static JavaMailSender javaMailSender;
    private static TemplateEngine templateEngine;

    public static void sendTextMail(String toEmail, String subject, String message){
        MailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send((SimpleMailMessage) mailMessage);
    }

    public static void sendHtmlMail(String toEmail, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);
        javaMailSender.send(mimeMessage);
    }

    public static String readHtmlTemplateFile(String pathTemplate,Map<String, Object> attrs) {
    	final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    	Context context = new Context(null, attrs);
    	context.setVariable("baseUrl", baseUrl);
    	String process = templateEngine.process(pathTemplate, context);
        return process;
    }

	public static void setJavaMailSender(JavaMailSender javaMailSender) {
		EmailUtil.javaMailSender = javaMailSender;
	}

	public static void setTemplateEngine(TemplateEngine templateEngine) {
		EmailUtil.templateEngine = templateEngine;
	}
    
    
}
