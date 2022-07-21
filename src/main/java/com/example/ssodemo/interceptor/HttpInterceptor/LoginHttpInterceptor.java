package com.example.ssodemo.interceptor.HttpInterceptor;

import com.example.ssodemo.interceptor.LoginContext;
import com.example.ssodemo.model.UserDetailModel;
import com.example.ssodemo.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 前端请求拦截器
 * @Author: Yihan Chen
 * @Date: 2022/7/19 10:47
 */
@Slf4j
@Component
public class LoginHttpInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LoginContext loginContext = new LoginContext();
        //从request中得到用户信息
        UserDetailModel user = loginService.getUserInfoFromToken(loginService.getToken(request));
        //如果token解析失败则重定向到登陆页
        if (user == null) {
            response.sendRedirect("http://localhost");
            return true;
        }
        //将信息存放在ThreadLocal中
        loginContext.setUserID(user.getUserID());
        loginContext.setFamilyID(user.getFamilyID());
        LoginContext.setContext(loginContext);
        //每次请求之后刷新token
        loginService.setNewToken(user, request, response);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginContext.remove();
    }
}
