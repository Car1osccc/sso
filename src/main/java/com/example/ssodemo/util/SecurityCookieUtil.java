package com.example.ssodemo.util;

import com.alibaba.fastjson.JSON;
import com.example.ssodemo.constants.CookieConstant;
import com.example.ssodemo.interceptor.LoginContext;
import com.example.ssodemo.model.UserDetailModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 加密工具类
 * @author Yihan Chen
 * @date 2022/7/18 13:37
 */
@Slf4j
public class SecurityCookieUtil {

    /**
     * md5加密后长度为32位
     */
    private final static int DEFAULT_LENGTH = 32;
    
    /**
     * 将一个object通过RC4加密
     * @param object 需加密的Object对象
     * @return 加密后的字符串
     */
    public static String encryptUserInfo(Object object) {
        String cookie = JSON.toJSONString(object);
        byte[] cookieByte = cookie.getBytes(StandardCharsets.UTF_8);
        byte[] keyByte = CookieConstant.getKey().getBytes(StandardCharsets.UTF_8);
        String code = new String(RC4Base(cookieByte, keyByte));
        return URLEncoder.encode(code, StandardCharsets.UTF_8);
    }
    
    /**
     * 根据加密后的信息以及当前时间通过md5加密组合成为token
     * @param encryptedInfo 加密信息
     * @return Token
     */
    public static String generateToken(String encryptedInfo) {
        String time = String.format("%015d", System.currentTimeMillis());
        String encryptBase = time + encryptedInfo;
        String md5Str = DigestUtils.md5DigestAsHex(encryptBase.getBytes(StandardCharsets.UTF_8));
        return md5Str + encryptBase;
    }

    /**
     * 从密文中解密出用户信息
     * @param encryptedInfo 加密的用户信息
     * @return 用户信息模型
     */
    public static LoginContext getEncryptedInfo(String encryptedInfo) {

        String decodeCookie = URLDecoder.decode(encryptedInfo, StandardCharsets.UTF_8);
        byte[] cookieByte = decodeCookie.getBytes(StandardCharsets.UTF_8);
        byte[] keyByte = CookieConstant.getKey().getBytes(StandardCharsets.UTF_8);
        String userString = new String(RC4Base(cookieByte, keyByte));
        try {
            return JSON.parseObject(userString, LoginContext.class);
        } catch (Exception e) {
            log.error("未找到token对应账号");
            return null;
        }
    }

    /**
     * 检查token是否有效，有效则返回对应的用户信息
     * @param token 登陆Token
     * @return 用户信息模型
     */
    public static LoginContext checkToken(String token) {
        //长度不符合则无效
        if (token == null || token.length() < DEFAULT_LENGTH) {
            log.error("未检测到token或token长度不符合");
            return null;
        }
        //分割token为md5Str以及用户信息密文
        String md5Str = token.substring(0, DEFAULT_LENGTH);
        String encryptBase = token.substring(DEFAULT_LENGTH);
        //将用户信息密闻加密后与md5Str进行比对
        if (!DigestUtils.md5DigestAsHex(encryptBase.getBytes(StandardCharsets.UTF_8)).equals(md5Str)) {
            log.error("非法token:{}", token);
            return null;
        }
        //检测密文中的时间判断token是否过期
        String time = token.substring(DEFAULT_LENGTH, DEFAULT_LENGTH + 15);
        if ((System.currentTimeMillis() - Long.parseLong(time)) > CookieConstant.EXPIRE_TIME) {
            log.error("检测到超时token:{}", token);
            return null;
        }
        //解密用户信息
        String encryptedInfo = token.substring(DEFAULT_LENGTH + 15);
        return getEncryptedInfo(encryptedInfo);
    }

    /**
     * RC4加密算法
     * @param input 加密信息
     * @param key 加密key
     * @return 加密数据
     */
    private static byte[] RC4Base(byte[] input, byte[] key) {
        int x = 0;
        int y = 0;
        int xorIndex;
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            key[x] = key[y];
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }
}
