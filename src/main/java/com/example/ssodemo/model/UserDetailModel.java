package com.example.ssodemo.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Yihan Chen
 * @date 2022/7/19 15:03
 */
@Data
public class UserDetailModel {

    /**
     * 用户ID
     */
    private Integer userID;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 手机号码
     */
    private String userTelephone;

    /**
     * 用户地址
     */
    private String userAddress;

    /**
     * 用户性别
     */
    private String userGender;

    /**
     * 所在家庭ID
     */
    private Integer familyID;

    /**
     * 用户注册时间
     */
    private Timestamp userRegisterTime;

}
