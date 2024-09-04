package com.tobioxd.BE.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobioxd.BE.services.oauth2.security.OAuth2UserCustomServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/user/info")
@RequiredArgsConstructor
public class AuthController {

    private final OAuth2UserCustomServiceImpl oAuth2UserCustomService;

    @PostMapping("")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(oAuth2UserCustomService.loginUser(request));
    }
}