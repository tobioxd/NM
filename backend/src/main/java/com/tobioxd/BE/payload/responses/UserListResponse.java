package com.tobioxd.BE.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor

public class UserListResponse {

    private List<UserListResponse> users;

    private int totalPages;
    
}
