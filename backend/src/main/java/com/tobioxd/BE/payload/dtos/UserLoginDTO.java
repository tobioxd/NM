package com.tobioxd.BE.payload.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserLoginDTO {

    @JsonProperty("input")
    @NotBlank(message = "Phone number or Email is required")
    private String input;

    @NotBlank(message = "Password cannot be blank")
    private String password;

}
