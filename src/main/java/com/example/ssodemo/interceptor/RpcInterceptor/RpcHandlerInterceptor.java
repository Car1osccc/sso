package com.example.ssodemo.interceptor.RpcInterceptor;

import com.alibaba.fastjson.JSON;
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
public class RpcHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LoginContext loginContext = new LoginContext();
        UserDetailModel user = JSON.parseObject(request.getHeader("userInfo"),UserDetailModel.class);
        if (user == null) {
            log.error("RPC调用接收到错误的userInfo");
            return false;
        }
        loginContext.setUserID(user.getUserID());
        loginContext.setFamilyID(user.getFamilyID());
        LoginContext.setContext(loginContext);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginContext.remove();
    }
}
