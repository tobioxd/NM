package com.tobioxd.BE.services.base;

import org.springframework.web.multipart.MultipartFile;

import com.tobioxd.BE.payload.responses.UserResponse;

public interface IFileService {

    public UserResponse uploadFile(MultipartFile file, String token) throws Exception;
    
} 
