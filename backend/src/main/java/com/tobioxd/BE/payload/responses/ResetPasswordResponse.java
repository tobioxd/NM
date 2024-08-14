package com.tobioxd.BE.payload.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ResetPasswordResponse {

    @JsonProperty("message")
    private String message;
    
}