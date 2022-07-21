package com.example.ssodemo.service;

import com.example.ssodemo.interceptor.LoginContext;
import com.example.ssodemo.model.UserDetailModel;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author PrettyBoy
 */

public interface LoginService {

    /**
     * 从Token中解析出用户信息
     * @param token 登陆Token
     * @return 用户信息模型
     */
    LoginContext getUserInfoFromToken(String token);

    /**
     * 登出
     * @param response 响应
     */
    void logout(HttpServletResponse response);

    String generateToken(LoginContext user);


}
