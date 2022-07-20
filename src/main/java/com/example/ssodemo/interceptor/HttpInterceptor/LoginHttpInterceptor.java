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
 * @Author: Yihan Chen
 * @Date: 2022/7/19 10:47
 */
@Slf4j
@Component
public class LoginHttpInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LoginContext loginContext = new LoginContext();
        UserDetailModel user = userService.getUserInfoFromToken(userService.getToken(request));
        if (user == null) {
            response.sendRedirect("http://localhost");
            return true;
        }
        loginContext.setUserID(user.getUserID());
        loginContext.setFamilyID(user.getFamilyID());
        LoginContext.setContext(loginContext);
        userService.setNewToken(user, request, response);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginContext.remove();
    }
}
