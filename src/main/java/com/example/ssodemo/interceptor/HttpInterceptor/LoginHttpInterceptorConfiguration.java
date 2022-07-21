package com.example.ssodemo.interceptor.HttpInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


/**
 * @Author: Yihan Chen
 * @Date: 2022/7/19 10:47
 */
@Configuration
public class LoginHttpInterceptorConfiguration implements WebMvcConfigurer {

    @Resource
    private LoginHttpInterceptor loginHttpInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginHttpInterceptor)
                .excludePathPatterns("/api")
                .excludePathPatterns("/v1/user/register")
                .excludePathPatterns("/v1/user/logout")
                .excludePathPatterns("/v1/user/loginCheck")
                .excludePathPatterns("/*.html");

    }

    public void setLoginContextInterceptor(LoginHttpInterceptor loginHttpInterceptor) {
        this.loginHttpInterceptor = loginHttpInterceptor;
    }
}
