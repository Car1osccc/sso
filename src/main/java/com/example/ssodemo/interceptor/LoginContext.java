package com.example.ssodemo.interceptor;

import lombok.Data;

/**
 * @Author: Yihan Chen
 * @Date: 2022/7/19 10:47
 */
@Data
public class LoginContext {

    private static final ThreadLocal<LoginContext> THREAD_LOCAL = new ThreadLocal<>();

    private String requestHost;

    private Integer userID;

    private Integer familyID;

    public static LoginContext getContext(){
        return LoginContext.THREAD_LOCAL.get();
    }

    public static void setContext(LoginContext loginContext){
        THREAD_LOCAL.set(loginContext);
    }

    public static void remove(){
        THREAD_LOCAL.remove();
    }

}
