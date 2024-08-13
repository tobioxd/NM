package com.tobioxd.BE.services.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import com.tobioxd.BE.payload.dtos.RefreshTokenDTO;
import com.tobioxd.BE.payload.dtos.ResetPasswordDTO;
import com.tobioxd.BE.payload.dtos.UpdatePasswordDTO;
import com.tobioxd.BE.payload.dtos.UserDTO;
import com.tobioxd.BE.payload.dtos.UserLoginDTO;
import com.tobioxd.BE.entities.User;
import com.tobioxd.BE.payload.responses.ForgotPasswordResponse;
import com.tobioxd.BE.payload.responses.LoginResponse;
import com.tobioxd.BE.payload.responses.RegisterResponse;
import com.tobioxd.BE.payload.responses.UpdatePasswordResponse;
import com.tobioxd.BE.payload.responses.UserListResponse;
import com.tobioxd.BE.payload.responses.UserResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface IUserService {

    RegisterResponse createUser(UserDTO userDTO, BindingResult result) throws Exception;

    LoginResponse loginUser(UserLoginDTO userLoginDTO) throws Exception;

    LoginResponse refreshToken(RefreshTokenDTO refreshTokenDTO) throws Exception;

    User getUserDetailsFromToken(String token) throws Exception;

    User getUserDetailsFromRefreshToken(String refreshToken) throws Exception;

    UserResponse updatePassword(UpdatePasswordDTO updatePasswordDTO, String token) throws Exception;

    Page<User> findAll(String keyword, Pageable pageable) throws Exception;

    public void blockOrEnable(String userId, boolean active) throws Exception;

    UserListResponse getAllUser(String keyword, int page, int limit) throws Exception;

    String blockOrEnableUser(String userId, boolean active) throws Exception;

    UpdatePasswordResponse updateMe(String token, UpdatePasswordDTO updatePasswordDTO, BindingResult result) throws Exception;

    RegisterResponse createReceptionist(UserDTO userDTO, BindingResult result) throws Exception;

    ForgotPasswordResponse forgotPassword(HttpServletRequest request, String phoneNumber) throws Exception;

    UpdatePasswordResponse resetPassword(String token, ResetPasswordDTO resetPasswordDTO, BindingResult result) throws Exception;

}
