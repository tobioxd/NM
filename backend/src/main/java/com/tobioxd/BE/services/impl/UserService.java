package com.tobioxd.BE.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.tobioxd.BE.config.security.JwtTokenUtil;
import com.tobioxd.BE.payload.dtos.RefreshTokenDTO;
import com.tobioxd.BE.payload.dtos.UpdatePasswordDTO;
import com.tobioxd.BE.payload.dtos.UserDTO;
import com.tobioxd.BE.payload.dtos.UserLoginDTO;
import com.tobioxd.BE.entities.Token;
import com.tobioxd.BE.entities.User;
import com.tobioxd.BE.exceptions.DataExistAlreadyException;
import com.tobioxd.BE.exceptions.DataNotFoundException;
import com.tobioxd.BE.repositories.TokenRepository;
import com.tobioxd.BE.repositories.UserRepository;
import com.tobioxd.BE.payload.responses.LoginResponse;
import com.tobioxd.BE.payload.responses.RegisterResponse;
import com.tobioxd.BE.payload.responses.UserListResponse;
import com.tobioxd.BE.payload.responses.UserResponse;
import com.tobioxd.BE.services.base.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    @Transactional
    public RegisterResponse createUser(UserDTO userDTO, BindingResult result) throws Exception {

        RegisterResponse registerResponse = new RegisterResponse();

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            throw new Exception(errorMessages.toString());
        }

        if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
            throw new Exception("Password do not match !");
        }
        // register user
        String phoneNumber = userDTO.getPhoneNumber();
        // Check if phonenumber exists already
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataExistAlreadyException("Phone number exists already !");
        }
        // convert from userDTO => user
        User newUser = User.builder()
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .isActive(true)
                .createdAt(new Date())
                .role("user")
                .build();

        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);

        User user = userRepository.save(newUser);
        registerResponse.setMessage("Register successfully !");
        registerResponse.setUser(user);
        return registerResponse;
    }

    @Override
    public LoginResponse loginUser(UserLoginDTO userLoginDTO) throws Exception {
        String phoneNumber = userLoginDTO.getPhoneNumber();
        String password = userLoginDTO.getPassword();

        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isEmpty()) {
            throw new DataNotFoundException("Invalid phonenuber/password !");
        }

        List<Token> tokens = tokenService.findByUser(user.get());

        if (tokens.size() >= 3) {
            tokens.sort((t1, t2) -> t2.getExpirationDate().compareTo(t1.getExpirationDate()));
            tokenService.deleteToken(tokens.get(0));
        }

        User existinguser = user.get();

        if (!passwordEncoder.matches(password, existinguser.getPassword())) {
            throw new DataNotFoundException("Invalid phonenuber/password !");
        }

        User existingUser = user.orElseThrow(() -> new DataNotFoundException("Invalid phonenuber/password !"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phoneNumber,
                password, existingUser.getAuthorities());

        authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenUtil.generateToken(existinguser);

        User userDetail = getUserDetailsFromToken(token);
        Token jwtToken = tokenService.addToken(userDetail, token);

        return LoginResponse.builder()
                .message("Login successfully !")
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .username(userDetail.getUsername())
                .roles(userDetail.getAuthorities().stream().map(item -> item.getAuthority()).toList())
                .id(userDetail.getId())
                .build();

    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new Exception("Token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isPresent()) {

            if (user.get().isActive() == false) {
                throw new Exception("User is blocked !");
            }

            return user.get();
        } else {
            throw new Exception("User not found");
        }
    }

    @Override
    public User getUserDetailsFromRefreshToken(String refreshToken) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        return getUserDetailsFromToken(existingToken.getToken());
    }

    @Override
    public Page<User> findAll(String keyword, Pageable pageable) throws Exception {
        return userRepository.findAll(keyword, pageable);
    }

    @Override
    @Transactional
    public void blockOrEnable(String userId, boolean active) throws Exception {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found !"));
        existingUser.setActive(active);
        userRepository.save(existingUser);
    }


    @Override
    public User updatePassword(UpdatePasswordDTO updatePasswordDTO, String token) throws Exception {
        String extractedToken = token.substring(7); // Clear "Bearer" from token
        User user = getUserDetailsFromToken(extractedToken);

        if (!passwordEncoder.matches(updatePasswordDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Old Password is incorrect !");
        }

        String password = updatePasswordDTO.getNewPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        return userRepository.save(user);

    }

    @Override
    public LoginResponse refreshToken(RefreshTokenDTO refreshTokenDTO) throws Exception {

        User userDetail = getUserDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken());
        Token jwtToken = tokenService.refreshToken(refreshTokenDTO.getRefreshToken(), userDetail);

        return LoginResponse.builder()
                .message("Refresh token successfully")
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .username(userDetail.getUsername())
                .roles(userDetail.getAuthorities().stream().map(item -> item.getAuthority()).toList())
                .id(userDetail.getId())
                .build();

    }

    @Override
    public UserListResponse getAllUser(String keyword, int page, int limit) throws Exception {

        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").descending());

        Page<UserResponse> users = findAll(keyword, pageRequest).map(UserResponse::fromUser);

        int totalPages = users.getTotalPages();
        List<UserResponse> userResponses = users.getContent();

        return UserListResponse.builder()
                .users(userResponses)
                .totalPages(totalPages)
                .build();
    }

    @Override
    public String blockOrEnableUser(String userId, boolean active) throws Exception {
        blockOrEnable(userId, active);
        return active ? "User is enabled !" : "User is blocked !";
    }

    @Override
    public RegisterResponse updateMe(String token, UpdatePasswordDTO updatePasswordDTO, BindingResult result)
            throws Exception {

        RegisterResponse registerResponse = new RegisterResponse();

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            throw new Exception(errorMessages.toString());
        }

        User user = updatePassword(updatePasswordDTO, token);
        registerResponse.setMessage("Update successfully !");
        registerResponse.setUser(user);

        return registerResponse;

    }

    @Override
    public RegisterResponse createReceptionist(UserDTO userDTO, BindingResult result) throws Exception {
        
        RegisterResponse registerResponse = new RegisterResponse();

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            throw new Exception(errorMessages.toString());
        }

        if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
            throw new Exception("Password do not match !");
        }
        // register user
        String phoneNumber = userDTO.getPhoneNumber();
        // Check if phonenumber exists already
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataExistAlreadyException("Phone number exists already !");
        }
        // convert from userDTO => user
        User newUser = User.builder()
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .isActive(true)
                .createdAt(new Date())
                .role("receptionist")
                .build();

        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);

        User user = userRepository.save(newUser);
        registerResponse.setMessage("Create receptionist account successfully !");
        registerResponse.setUser(user);
        return registerResponse;

    }

}
