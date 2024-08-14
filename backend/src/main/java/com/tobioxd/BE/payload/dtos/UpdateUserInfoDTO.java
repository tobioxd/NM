package com.tobioxd.BE.payload.dtos;

import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UpdateUserInfoDTO {

    private String name;

    private String email;

}
