package com.example.ssodemo.service.serviceImpl;

import com.example.ssodemo.interceptor.LoginContext;
import com.example.ssodemo.service.LoginService;
import com.example.ssodemo.util.SecurityCookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yihan Chen
 * @date 2022/7/6 10:50
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public LoginContext getUserInfoFromToken(String token) {
        return SecurityCookieUtil.checkToken(token);
    }

    @Override
    public void logout(HttpServletResponse response) {
        Cookie clearCookie = new Cookie("userToken", null);
        clearCookie.setMaxAge(0);
        clearCookie.setDomain("localhost");
        clearCookie.setPath("/");
        response.addCookie(clearCookie);
        try {
            response.sendRedirect("http://localhost");
        } catch (Exception e) {
            log.error("重定向失败", e);
        }
    }

    @Override
    public String generateToken(LoginContext user) {
        String encryptUserInfo = SecurityCookieUtil.encryptUserInfo(user);
        return SecurityCookieUtil.generateToken(encryptUserInfo);
    }
}