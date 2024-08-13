package com.tobioxd.BE.payload.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UpdatePasswordDTO {

    @NotBlank
    private String password;

    @NotBlank
    private String newPassword;

}
