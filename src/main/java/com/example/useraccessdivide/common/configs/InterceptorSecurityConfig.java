package com.example.useraccessdivide.common.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.useraccessdivide.common.interceptors.AuthenticationInterceptor;
import com.example.useraccessdivide.common.interceptors.LoggedInterceptor;

/**
 * Class cấu hình filter
 * @author tranh
 *
 */
@Configuration
public class InterceptorSecurityConfig implements WebMvcConfigurer {
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;
    @Autowired
    private LoggedInterceptor loggedInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/user", "/api/user/**", "/api/admin/**")
                .excludePathPatterns("/api/user/login", "/api/user/register", "/api/user/forget-password", "/api/user/get-jwt");

//        registry.addInterceptor(loggedInterceptor)
//                .addPathPatterns("/api/user/login")
//                .addPathPatterns("/api/user/register")
//                .addPathPatterns("/api/user/forget-password");
    }
}
