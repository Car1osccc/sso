package com.example.ssodemo.util;

import com.alibaba.fastjson.JSON;
import com.example.ssodemo.constants.CookieConstants;
import com.example.ssodemo.model.UserDetailModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 加密工具类
 * @Author: Yihan Chen
 * @Date: 2022/7/18 13:37
 */
@Slf4j
public class SecurityCookieUtil {

    //md5加密后长度为32位
    private final static int defaultLength = 32;

    //将一个object通过RC4加密
    public static String encryptUserInfo(Object object) {
        String cookie = JSON.toJSONString(object);
        byte[] cookieByte = cookie.getBytes(StandardCharsets.UTF_8);
        byte[] keyByte = CookieConstants.getKey().getBytes(StandardCharsets.UTF_8);
        String code = new String(RC4Base(cookieByte, keyByte));
        return URLEncoder.encode(code, StandardCharsets.UTF_8);
    }

    //根据加密后的信息以及当前时间通过md5加密组合成为token
    public static String generateToken(String encryptedInfo) {
        String time = String.format("%015d", new Date().getTime());
        String encryptBase = time + encryptedInfo;
        String md5Str = DigestUtils.md5DigestAsHex(encryptBase.getBytes(StandardCharsets.UTF_8));
        return md5Str + encryptBase;
    }

    //从密文中解密出用户信息
    public static UserDetailModel getEncryptedInfo(String encryptedInfo) {

        String decodeCookie = URLDecoder.decode(encryptedInfo, StandardCharsets.UTF_8);
        byte[] cookieByte = decodeCookie.getBytes(StandardCharsets.UTF_8);
        byte[] keyByte = CookieConstants.getKey().getBytes(StandardCharsets.UTF_8);
        String userString = new String(RC4Base(cookieByte, keyByte));
        try {
            return JSON.parseObject(userString, UserDetailModel.class);
        } catch (Exception e) {
            log.error("未找到token对应账号");
            return null;
        }
    }

    //检查token是否有效，有效则返回对应的用户信息
    public static UserDetailModel checkToken(String token) {
        //长度不符合则无效
        if (token == null || token.length() < defaultLength ) {
            log.error("未检测到token或token长度不符合");
            return null;
        }
        //分割token为md5Str以及用户信息密文
        String md5Str = token.substring(0, defaultLength);
        String encryptBase = token.substring(defaultLength);
        //将用户信息密闻加密后与md5Str进行比对
        if (!DigestUtils.md5DigestAsHex(encryptBase.getBytes(StandardCharsets.UTF_8)).equals(md5Str)) {
            log.error("非法token:{}", token);
            return null;
        }
        //检测密文中的时间判断token是否过期
        String time = token.substring(defaultLength, defaultLength + 15);
        if ((new Date().getTime() - Long.parseLong(time)) > 30 * 60 * 1000) {
            log.error("检测到超时token:{}", token);
            return null;
        }
        //解密用户信息
        String encryptedInfo = token.substring(defaultLength + 15);
        return getEncryptedInfo(encryptedInfo);
    }

    //RC4加密算法
    private static byte[] RC4Base(byte[] input, byte[] key) {
        int x = 0;
        int y = 0;
        int xorIndex;
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte temp = key[x];
            key[x] = key[y];
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }
}
