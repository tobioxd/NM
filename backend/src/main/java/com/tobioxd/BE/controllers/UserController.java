package com.tobioxd.BE.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.tobioxd.BE.payload.dtos.ForgotPasswordDTO;
import com.tobioxd.BE.payload.dtos.RefreshTokenDTO;
import com.tobioxd.BE.payload.dtos.ResetPasswordDTO;
import com.tobioxd.BE.payload.dtos.UpdatePasswordDTO;
import com.tobioxd.BE.payload.dtos.UpdateUserInfoDTO;
import com.tobioxd.BE.payload.dtos.UserDTO;
import com.tobioxd.BE.payload.dtos.UserLoginDTO;
import com.tobioxd.BE.payload.responses.LoginResponse;
import com.tobioxd.BE.payload.responses.RegisterResponse;
import com.tobioxd.BE.payload.responses.UpdatePasswordResponse;
import com.tobioxd.BE.payload.responses.UserListResponse;
import com.tobioxd.BE.services.impl.UserServiceImpl;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor

public class UserController {

    private final UserServiceImpl UserServiceImpl;

    @PostMapping("/register")
    @Operation(summary = "Register account")
    public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserServiceImpl.createUser(userDTO, result));
    }

    @PostMapping("/login")
    @Operation(summary = "Login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserServiceImpl.loginUser(userLoginDTO));
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "Refresh Token")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) throws Exception {
        return ResponseEntity.ok(UserServiceImpl.refreshToken(refreshTokenDTO));
    }

    @PatchMapping("/updatePassword")
    @Operation(summary = "Update user information")
    public ResponseEntity<UpdatePasswordResponse> updateMe(@RequestHeader("Authorization") String token, @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO, BindingResult result) throws Exception {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserServiceImpl.updateMe(token, updatePasswordDTO, result));
    }

    @PatchMapping("/updateMe")
    @Operation(summary = "Update user information")
    public ResponseEntity<?> updateMe(@RequestHeader("Authorization") String token, @Valid @RequestBody UpdateUserInfoDTO userDTO, BindingResult result) throws Exception {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserServiceImpl.updateUserInfor(userDTO, token));
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users")
    public ResponseEntity<UserListResponse> getAllUser(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(UserServiceImpl.getAllUser(keyword, page, limit));

    }

    @PatchMapping("/blockOrEnable/{userId}/{active}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Block or enable user")
    public ResponseEntity<?> blockOrEnable(@PathVariable String userId, @PathVariable boolean active) throws Exception {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserServiceImpl.blockOrEnableUser(userId, active));
    }

    @PostMapping("/createreceptionist")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create Receptionist account")
    public ResponseEntity<RegisterResponse> createReceptionist(@Valid @RequestBody UserDTO userDTO, BindingResult result) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserServiceImpl.createReceptionist(userDTO, result));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password")
    public ResponseEntity<?> forgotPassword(HttpServletRequest request, @RequestBody ForgotPasswordDTO forgotPasswordDTO, BindingResult result) throws Exception {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserServiceImpl.forgotPassword(request, forgotPasswordDTO, result));
    }

    @PatchMapping("/reset-password/{token}")
    @Operation(summary = "Reset password")
    public ResponseEntity<?> resetPassword(@PathVariable String token, @Valid @RequestBody ResetPasswordDTO resetPasswordDTO, BindingResult result) throws Exception {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserServiceImpl.resetPassword(token, resetPasswordDTO, result));
    }

}
