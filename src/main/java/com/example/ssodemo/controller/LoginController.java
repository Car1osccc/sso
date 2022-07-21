package com.example.ssodemo.controller;

import com.example.ssodemo.interceptor.LoginContext;
import com.example.ssodemo.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yihan Chen
 * @date 2022/7/5 15:50
 */
@Slf4j
@RestController
@RequestMapping("/sso")
public class LoginController {

    @Resource
    private LoginService loginService;

    /**
     * 从request的cookie中得到用户信息
     */
    @GetMapping("/checkToken")
    public LoginContext getUserFromToken(String token) {
        return loginService.getUserInfoFromToken(token);
    }

    @RequestMapping("/logout")
    public void logout(HttpServletResponse response){
        loginService.logout(response);
    }

    @RequestMapping("/getNewToken")
    public String getNewToken(String token){
        LoginContext context = loginService.getUserInfoFromToken(token);
        return loginService.generateToken(context);
    }

    @RequestMapping(value = "/generateToken")
    public String generateToken(@RequestParam Integer userID,
                                @RequestParam Integer familyID,
                                @RequestParam String userName){
        LoginContext loginContext = new LoginContext();
        loginContext.setUserID(userID);
        loginContext.setFamilyID(familyID);
        loginContext.setUserName(userName);
        return loginService.generateToken(loginContext);
    }
    @RequestMapping("/getInfo")
    public LoginContext loginContext(){
        return LoginContext.getContext();
    }

}
