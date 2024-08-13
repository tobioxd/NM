package com.tobioxd.BE.payload.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class UserDTO {
    
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @JsonProperty("password")
    @NotBlank(message = "Password is required")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("name")
    @NotBlank(message = "Name is required")
    private String name;

}
