package com.example.demo20250620.config;

import com.example.demo20250620.interceptor.SessionTimeoutInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private SessionTimeoutInterceptor sessionTimeoutInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionTimeoutInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/logout", "/css/**", "/js/**", "/images/**", "/app/**");
    }
}