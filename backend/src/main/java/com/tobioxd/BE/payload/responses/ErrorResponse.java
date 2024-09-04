package com.tobioxd.BE.payload.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ErrorResponse {

    private int status;

    private String message;
}