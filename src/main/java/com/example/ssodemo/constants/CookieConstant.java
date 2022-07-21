package com.example.ssodemo.constants;


/**
 * 加密用户信息的Key
 * @author Yihan Chen
 * @date 2022/7/18 13:44
 */

public class CookieConstant {
    private static final String KEY = "CarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarlosccc" +
            "CarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarlosccc" +
            "CarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarlosccc" +
            "CarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarlosccc" +
            "CarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarloscccCarlosccc";
    public static String getKey(){
        return KEY;
    }
    public static Integer EXPIRE_TIME = 30 * 60 * 1000;
    
}
