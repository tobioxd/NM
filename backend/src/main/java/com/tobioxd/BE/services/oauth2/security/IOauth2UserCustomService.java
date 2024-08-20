package com.tobioxd.BE.services.oauth2.security;

import com.tobioxd.BE.entities.User;
import com.tobioxd.BE.payload.responses.LoginResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface IOauth2UserCustomService {

    public User getUserInfo(HttpServletRequest request);

    public LoginResponse loginUser(HttpServletRequest request)  throws Exception; 

}
