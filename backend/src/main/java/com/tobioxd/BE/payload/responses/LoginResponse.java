package com.tobioxd.BE.payload.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LoginResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("token")
    private String token;

    @JsonProperty("refresh_token")
    private String refreshToken;

    private String tokenType = "Bearer";

    private String id;

    private UserResponse user;

    private List<String> roles;
}