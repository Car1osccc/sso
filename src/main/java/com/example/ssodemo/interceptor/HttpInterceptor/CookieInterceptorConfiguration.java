package com.example.ssodemo.interceptor.HttpInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


/**
 * @author Yihan Chen
 * @date 2022/7/19 10:47
 */
@Configuration
public class CookieInterceptorConfiguration implements WebMvcConfigurer {

    @Resource
    private CookieInterceptor cookieInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cookieInterceptor)
                .excludePathPatterns("/sso/generateToken");
    }

    public void setLoginContextInterceptor(CookieInterceptor cookieInterceptor) {
        this.cookieInterceptor = cookieInterceptor;
    }
}
