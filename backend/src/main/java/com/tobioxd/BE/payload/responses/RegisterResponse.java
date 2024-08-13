package com.tobioxd.BE.payload.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tobioxd.BE.entities.User;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegisterResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;
    
}