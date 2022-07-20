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
 * @Author: Yihan Chen
 * @Date: 2022/7/18 13:37
 */
@Slf4j
public class SecurityCookieUtil {
    private final static int defaultLength = 32;


    public static String encryptUserInfo(Object object) {
        String cookie = JSON.toJSONString(object);
        byte[] cookieByte = cookie.getBytes(StandardCharsets.UTF_8);
        byte[] keyByte = CookieConstants.getKey().getBytes(StandardCharsets.UTF_8);
        String code = new String(RC4Base(cookieByte, keyByte));
        return URLEncoder.encode(code, StandardCharsets.UTF_8);
    }

    public static String generateToken(String encryptedInfo) {
        String time = String.format("%015d", new Date().getTime());
        String encryptBase = time + encryptedInfo;
        String md5Str = DigestUtils.md5DigestAsHex(encryptBase.getBytes(StandardCharsets.UTF_8));
        return md5Str + encryptBase;
    }

    public static UserDetailModel getEncryptedInfo(String encryptedInfo) {

        String decodeCookie = URLDecoder.decode(encryptedInfo, StandardCharsets.UTF_8);
        byte[] cookieByte = decodeCookie.getBytes(StandardCharsets.UTF_8);
        byte[] keyByte = CookieConstants.getKey().getBytes(StandardCharsets.UTF_8);
        String userString = new String(RC4Base(cookieByte, keyByte));
        try {
            return JSON.parseObject(userString,UserDetailModel.class);
        } catch (Exception e) {
            log.error("未找到token对应账号");
            return null;
        }
    }

    public static UserDetailModel checkToken(String token) {
        if (token.length() < defaultLength) {
            return null;
        }
        String md5Str = token.substring(0, defaultLength);
        String encryptBase = token.substring(defaultLength);
        if (!DigestUtils.md5DigestAsHex(encryptBase.getBytes(StandardCharsets.UTF_8)).equals(md5Str)) {
            log.error("非法token:{}", token);
            return null;
        }
        String time = token.substring(defaultLength, defaultLength + 15);
        if ((new Date().getTime() - Long.parseLong(time)) > 30 * 60 * 1000) {
            log.error("检测到超时token:{}", token);
            return null;
        }
        String encryptedInfo = token.substring(defaultLength + 15);
        return getEncryptedInfo(encryptedInfo);
    }

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
