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

import com.tobioxd.BE.payload.dtos.RefreshTokenDTO;
import com.tobioxd.BE.payload.dtos.ResetPasswordDTO;
import com.tobioxd.BE.payload.dtos.UpdatePasswordDTO;
import com.tobioxd.BE.payload.dtos.UpdateUserInfoDTO;
import com.tobioxd.BE.payload.dtos.UserDTO;
import com.tobioxd.BE.payload.dtos.UserLoginDTO;
import com.tobioxd.BE.exceptions.DataExistAlreadyException;
import com.tobioxd.BE.exceptions.DataNotFoundException;
import com.tobioxd.BE.exceptions.ExpiredTokenException;
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
    public ResponseEntity<RegisterResponse> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result) {
        RegisterResponse registerResponse = new RegisterResponse();
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(UserServiceImpl.createUser(userDTO, result));
        } catch (DataExistAlreadyException e) {
            registerResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(registerResponse);
        } catch (Exception e) {
            registerResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerResponse);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(UserServiceImpl.loginUser(userLoginDTO));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LoginResponse.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(LoginResponse.builder().message(e.getMessage()).build());
        }
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "Refresh Token")
    public ResponseEntity<LoginResponse> refreshToken(
            @Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        try {
            return ResponseEntity.ok(UserServiceImpl.refreshToken(refreshTokenDTO));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LoginResponse.builder().message(e.getMessage()).build());
        } catch (ExpiredTokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(LoginResponse.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder().message(e.getMessage()).build());
        }
    }

    @PatchMapping("/updatePasword")
    @Operation(summary = "Update user information")
    public ResponseEntity<UpdatePasswordResponse> updateMe(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO,
            BindingResult result) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(UserServiceImpl.updateMe(token, updatePasswordDTO, result));
        } catch (Exception e) {
            UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse();
            updatePasswordResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(updatePasswordResponse);
        }
    }

    @PatchMapping("/updateMe")
    @Operation(summary = "Update user information")
    public ResponseEntity<?> updateMe(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdateUserInfoDTO userDTO,
            BindingResult result) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserServiceImpl.updateUserInfor(userDTO, token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users")
    public ResponseEntity<UserListResponse> getAllUser(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(UserServiceImpl.getAllUser(keyword, page, limit));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/blockOrEnable/{userId}/{active}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Block or enable user")
    public ResponseEntity<?> blockOrEnable(
            @PathVariable String userId,
            @PathVariable boolean active) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserServiceImpl.blockOrEnableUser(userId, active));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/createreceptionist")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create Receptionist account")
    public ResponseEntity<RegisterResponse> createReceptionist(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result) {
        RegisterResponse registerResponse = new RegisterResponse();
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(UserServiceImpl.createReceptionist(userDTO, result));
        } catch (DataExistAlreadyException e) {
            registerResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(registerResponse);
        } catch (Exception e) {
            registerResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerResponse);
        }
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password")
    public ResponseEntity<?> forgotPassword(HttpServletRequest request,
            @RequestParam String input) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserServiceImpl.forgotPassword(request,input));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/reset-password/{token}")
    @Operation(summary = "Reset password")
    public ResponseEntity<?> resetPassword(@PathVariable String token,
            @Valid @RequestBody ResetPasswordDTO resetPasswordDTO,
            BindingResult result) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserServiceImpl.resetPassword(token, resetPasswordDTO, result));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
