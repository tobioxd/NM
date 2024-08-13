package com.tobioxd.BE.payload.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tobioxd.BE.entities.User;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
        
    @JsonProperty("id")
    private String id;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("name")
    private String name;

    @JsonProperty("role")
    private String role;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("is_active")
    private boolean active;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .active(user.isActive())
                .build();
    }

}
