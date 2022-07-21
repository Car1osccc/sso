package com.example.ssodemo.service.serviceImpl;

import com.example.ssodemo.model.UserDetailModel;
import com.example.ssodemo.service.LoginService;
import com.example.ssodemo.util.SecurityCookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Yihan Chen
 * @Date: 2022/7/6 10:50
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public UserDetailModel getUserInfoFromToken(String token) {
        return SecurityCookieUtil.checkToken(token);
    }

    @Override
    public void setNewToken(UserDetailModel user, HttpServletRequest request, HttpServletResponse response){

        String newToken = SecurityCookieUtil.generateToken(SecurityCookieUtil.encryptUserInfo(user));
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userToken")){
                    cookie.setValue(newToken);
                    response.addCookie(cookie);
                    return;
                }
            }
        }
        response.addCookie(new Cookie("userToken",newToken));
    }

    @Override
    public String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userToken") && !cookie.getValue().isEmpty()){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void logout(HttpServletResponse response){
        Cookie clearCookie = new Cookie("userToken",null);
        clearCookie.setMaxAge(0);
        response.addCookie(clearCookie);
        try{
            response.sendRedirect("http://localhost");
        }catch (Exception e){
            log.error("重定向失败",e);
        }


    }
}