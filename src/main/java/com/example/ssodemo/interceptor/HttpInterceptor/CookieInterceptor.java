package com.example.ssodemo.interceptor.HttpInterceptor;

import com.example.ssodemo.interceptor.LoginContext;
import com.example.ssodemo.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 前端请求拦截器
 *
 * @author Yihan Chen
 * @date 2022/7/19 10:47
 */
@Slf4j
@Component
public class CookieInterceptor implements HandlerInterceptor {

    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从request中获取token
        LoginContext loginContext = new LoginContext();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("userToken".equals(cookie.getName())){
                loginContext = loginService.getUserInfoFromToken(cookie.getValue());
            }
        }
        LoginContext.setContext(loginContext);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LoginContext.remove();
    }
}
