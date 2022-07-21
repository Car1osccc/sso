package com.example.ssodemo.interceptor;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Yihan Chen
 * @date 2022/7/19 10:47
 */
@Data
public class LoginContext implements Serializable {

    private static final ThreadLocal<LoginContext> THREAD_LOCAL = new ThreadLocal<>();

    private Integer userID;

    private Integer familyID;

    private String UserName;

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
