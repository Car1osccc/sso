package com.example.ssodemo.controller;

import com.example.ssodemo.interceptor.LoginContext;
import com.example.ssodemo.model.UserDetailModel;
import com.example.ssodemo.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Yihan Chen
 * @Date: 2022/7/5 15:50
 */
@Slf4j
@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    //从request的cookie中得到用户信息
    public UserDetailModel getUserFromToken(HttpServletRequest request, HttpServletResponse response) {
        //将cookie转为模型
        UserDetailModel user = loginService.getUserInfoFromToken(loginService.getToken(request));
        if (user == null) {
            return null;
        }
        //设置新的token
        loginService.setNewToken(user, request, response);
        return user;
    }

    public LoginContext getInfoFromThreadLocal() {
        return LoginContext.getContext();
    }
}
