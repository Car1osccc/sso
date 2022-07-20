package com.example.ssodemo.interceptor.RpcInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


/**
 * @Author: Yihan Chen
 * @Date: 2022/7/19 10:47
 */
@Configuration
public class RpcInterceptorConfiguration implements WebMvcConfigurer {

    @Resource
    private RpcHandlerInterceptor rpcHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rpcHandlerInterceptor)
                .addPathPatterns("/api");

    }

    public void setLoginContextInterceptor(RpcHandlerInterceptor rpcHandlerInterceptor) {
        this.rpcHandlerInterceptor = rpcHandlerInterceptor;
    }
}
