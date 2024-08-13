package com.tobioxd.BE.payload.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ResetPasswordDTO {

    @NotBlank
    private String newPassword;

}
