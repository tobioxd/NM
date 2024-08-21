package com.tobioxd.BE.controllers;

import com.tobioxd.BE.payload.responses.UserResponse;
import com.tobioxd.BE.services.impl.FileServiceImpl;

import lombok.AllArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("${api.prefix}/users")

public class FileController {

    private final FileServiceImpl fileService;

    @PostMapping(value = "/uploading", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> generateUrl(@ModelAttribute("files") MultipartFile file,
            @RequestHeader("Authorization") String token) throws Exception {
        return ResponseEntity.ok(fileService.uploadFile(file, token));
    }

}