package com.tobioxd.BE.payload.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UpdatePasswordResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private UserResponse userResponse;
    
}