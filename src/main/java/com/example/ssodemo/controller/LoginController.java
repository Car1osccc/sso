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
    private LoginService userService;

    public String getUserFromToken(HttpServletRequest request, HttpServletResponse response){
        UserDetailModel user = userService.getUserInfoFromToken(userService.getToken(request));
        if (user == null) {
            return "Token验证失败";
        }
        userService.setNewToken(user, request, response);
        return user.toString();
    }

    public String spellToken(HttpServletRequest request){
        return request.getRequestURL().toString() + "?userToken=" + userService.getToken(request);
    }
    public LoginContext getInfoFromThreadLocal(){
        return LoginContext.getContext();
    }
}
