package com.tobioxd.BE.services.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.mail.javamail.JavaMailSender;

import com.tobioxd.BE.config.security.JwtTokenUtil;
import com.tobioxd.BE.payload.dtos.ForgotPasswordDTO;
import com.tobioxd.BE.payload.dtos.RefreshTokenDTO;
import com.tobioxd.BE.payload.dtos.ResetPasswordDTO;
import com.tobioxd.BE.payload.dtos.UpdatePasswordDTO;
import com.tobioxd.BE.payload.dtos.UpdateUserInfoDTO;
import com.tobioxd.BE.payload.dtos.UserDTO;
import com.tobioxd.BE.payload.dtos.UserLoginDTO;
import com.tobioxd.BE.entities.Token;
import com.tobioxd.BE.entities.User;
import com.tobioxd.BE.exceptions.DataExistAlreadyException;
import com.tobioxd.BE.exceptions.DataNotFoundException;
import com.tobioxd.BE.exceptions.ExpiredTokenException;
import com.tobioxd.BE.repositories.TokenRepository;
import com.tobioxd.BE.repositories.UserRepository;
import com.tobioxd.BE.payload.responses.ForgotPasswordResponse;
import com.tobioxd.BE.payload.responses.LoginResponse;
import com.tobioxd.BE.payload.responses.RegisterResponse;
import com.tobioxd.BE.payload.responses.UpdatePasswordResponse;
import com.tobioxd.BE.payload.responses.UserListResponse;
import com.tobioxd.BE.payload.responses.UserResponse;
import com.tobioxd.BE.services.base.IUserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final TokenServiceImpl tokenServiceImpl;
    private final JavaMailSender javaMailSender;

    @Value("${client.port}")
    private String port;

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
        // Check if email form is wrong
        if (!userDTO.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            throw new Exception("Invalid email format. Please provide a valid email.");
        }
        // Check if email exists already
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DataExistAlreadyException("Email exists already !");
        }

        // Check if password is valid
        if(!isPasswordValid(userDTO.getPassword())) {
            throw new Exception("Password must contain at least 8 characters, 1 uppercase letter, 1 number and 1 special character !");
        }

        // convert from userDTO => user
        User newUser = User.builder()
                .phoneNumber(userDTO.getPhoneNumber())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .photoUrl("default.jpg")
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
        String input = userLoginDTO.getInput();
        String password = userLoginDTO.getPassword();

        String phoneNumber;
        String email;

        if (input.matches("\\d+")) {
            phoneNumber = input;
            email = null;
        } else if (input.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            phoneNumber = null;
            email = input;
        } else {
            throw new IllegalArgumentException("Invalid input format. Please provide a valid phone number or email.");
        }

        Optional<User> user = Optional.empty();

        if (phoneNumber != null) {
            user = userRepository.findByPhoneNumber(phoneNumber);
        } else if (email != null) {
            user = userRepository.findByEmail(email);
        }

        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found !");
        }

        List<Token> tokens = tokenServiceImpl.findByUser(user.get());

        if (tokens.size() >= 3) {
            tokens.sort((t1, t2) -> t2.getExpirationDate().compareTo(t1.getExpirationDate()));
            tokenServiceImpl.deleteToken(tokens.get(0));
        }

        User existinguser = user.get();

        if (!passwordEncoder.matches(password, existinguser.getPassword())) {
            throw new DataNotFoundException("Invalid phonenumber/password !");
        }

        User existingUser = user.orElseThrow(() -> new DataNotFoundException("Invalid phonenumber/password !"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                existingUser.getEmail(),
                password, existingUser.getAuthorities());

        authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenUtil.generateToken(existingUser);

        User userDetail = getUserDetailsFromToken(token);
        Token jwtToken = tokenServiceImpl.addToken(userDetail, token);

        return LoginResponse.builder()
                .message("Login successfully !")
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .user(UserResponse.fromUser(userDetail))
                .roles(userDetail.getAuthorities().stream().map(item -> item.getAuthority()).toList())
                .id(userDetail.getId())
                .build();

    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new Exception("Token is expired");
        }
        String email = jwtTokenUtil.extractEmail(token);
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {

            if (user.get().isActive() == false) {
                throw new ExpiredTokenException("User is blocked !");
            }

            return user.get();
        } else {
            throw new DataNotFoundException("User not found");
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
    public UserResponse updatePassword(UpdatePasswordDTO updatePasswordDTO, String token) throws Exception {
        String extractedToken = token.substring(7); // Clear "Bearer" from token
        User user = getUserDetailsFromToken(extractedToken);

        if (!passwordEncoder.matches(updatePasswordDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Old Password is incorrect !");
        }

        String password = updatePasswordDTO.getNewPassword();

        if(!isPasswordValid(password)) {
            throw new Exception("Password must contain at least 8 characters, 1 uppercase letter, 1 number and 1 special character !");
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("New password must be different from the old password !");
        }

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setPasswordChangeAt(new Date());

        return UserResponse.fromUser(userRepository.save(user));

    }

    @Override
    public UserResponse updateUserInfor(UpdateUserInfoDTO updateUserInfoDTO, String token) throws Exception {
        String extractedToken = token.substring(7); // Clear "Bearer" from token
        User user = getUserDetailsFromToken(extractedToken);

        if (updateUserInfoDTO.getName() != null) {
            user.setName(updateUserInfoDTO.getName());
        }
        if (updateUserInfoDTO.getEmail() != null) {
            if(updateUserInfoDTO.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                if(userRepository.existsByEmail(updateUserInfoDTO.getEmail())) {
                    throw new DataExistAlreadyException("Email exists already !");
                }else{
                    user.setEmail(updateUserInfoDTO.getEmail());
                }
            } else {
                throw new Exception("Invalid email format. Please provide a valid email.");
            }
        }

        return UserResponse.fromUser(userRepository.save(user));
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenDTO refreshTokenDTO) throws Exception {

        User userDetail = getUserDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken());
        Token jwtToken = tokenServiceImpl.refreshToken(refreshTokenDTO.getRefreshToken(), userDetail);

        return LoginResponse.builder()
                .message("Refresh token successfully")
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .user(UserResponse.fromUser(userDetail))
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
    public UpdatePasswordResponse updateMe(String token, UpdatePasswordDTO updatePasswordDTO, BindingResult result)
            throws Exception {

        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse();

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            throw new Exception(errorMessages.toString());
        }

        UserResponse user = updatePassword(updatePasswordDTO, token);
        updatePasswordResponse.setMessage("Update successfully !");
        updatePasswordResponse.setUserResponse(user);

        return updatePasswordResponse;

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

    @Override
    public void sendEmail(User user, String content) throws Exception {
        try {

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("minhnhat.kd.hungyen@gmail.com");// input the senders email ID
            msg.setTo(user.getEmail());

            msg.setSubject("Reset Password");
            msg.setText("Hello " + user.getName() + "\n\n"
                    + "You have 5 minute to take new password !" + "\n\n"
                    + "Please click on this link to Reset your Password : " + content + ". \n\n"
                    + "Regards \n" + "tobioxd");

            javaMailSender.send(msg);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public ForgotPasswordResponse forgotPassword(HttpServletRequest request, ForgotPasswordDTO forgotPasswordDTO,
            BindingResult result) throws Exception {

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            throw new Exception(errorMessages.toString());
        }

        String input = forgotPasswordDTO.getInput();

        String clientIP = request.getServerName();

        ForgotPasswordResponse forgotPasswordResponse = new ForgotPasswordResponse();

        String phoneNumber;
        String email;

        if (input.matches("\\d+")) {
            phoneNumber = input;
            email = null;
        } else if (input.matches("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+")) {
            phoneNumber = null;
            email = input;
        } else {
            throw new IllegalArgumentException("Invalid input format. Please provide a valid phone number or email.");
        }

        Optional<User> user = Optional.empty();

        if (phoneNumber != null) {
            user = userRepository.findByPhoneNumber(phoneNumber);
        } else if (email != null) {
            user = userRepository.findByEmail(email);
        }

        User existingUser = user.orElseThrow(() -> new DataNotFoundException("User not found !"));
        String token = generateToken();
        existingUser.setPasswordResetToken(token);
        existingUser.setPasswordResetExpirationDate(java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(5)));
        userRepository.save(existingUser);

        String link = "http://" + clientIP + ":" + port + "/reset-password/" + token;

        sendEmail(existingUser, link);
        forgotPasswordResponse.setStatus("success");
        forgotPasswordResponse.setMessage("Password have sent to your device, You have 5 minute to take new password !");
        forgotPasswordResponse.setLink(link);

        return forgotPasswordResponse;

    }

    @Override
    public UpdatePasswordResponse resetPassword(String token, ResetPasswordDTO resetPasswordDTO, BindingResult result)
            throws Exception {

        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse();

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            throw new Exception(errorMessages.toString());
        }

        if(!isPasswordValid(resetPasswordDTO.getNewPassword())) {
            throw new Exception("Password must contain at least 8 characters, 1 uppercase letter, 1 number and 1 special character !");
        }

        Optional<User> user = userRepository.findByPasswordResetToken(token);

        if (user.isEmpty()) {
            throw new DataNotFoundException("Link is invalid !");
        }

        User existingUser = user.get();

        if (isTokenExpired(existingUser.getPasswordResetExpirationDate())) {
            throw new Exception("Link is expired !");
        }

        String password = resetPasswordDTO.getNewPassword();
        String encodedPassword = passwordEncoder.encode(password);
        existingUser.setPassword(encodedPassword);
        existingUser.setPasswordResetToken(null);
        existingUser.setPasswordResetExpirationDate(null);
        userRepository.save(existingUser);
        updatePasswordResponse.setMessage("Update password successfully, please log in again !");

        return updatePasswordResponse;

    }

    private String generateToken() {
        StringBuilder token = new StringBuilder();

        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }

    private boolean isTokenExpired(Date passwordResetExpirationDate) {

        Date now = new Date();
        return now.after(passwordResetExpirationDate);

    }

    private boolean isPasswordValid(String password) {
        // Regex to check valid password.
        String regex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$&*]).{8,}$";
        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        return p.matcher(password).matches();
    }

}
