package com.example.ssodemo.service;


import com.example.ssodemo.model.UserDetailModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {

    UserDetailModel getUserInfoFromToken(String token);
    void setNewToken(UserDetailModel user, HttpServletRequest request, HttpServletResponse response);
    String getToken(HttpServletRequest request);
}
