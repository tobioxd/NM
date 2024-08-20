package com.tobioxd.BE.services.oauth2.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.tobioxd.BE.config.security.JwtTokenUtil;
import com.tobioxd.BE.entities.Token;
import com.tobioxd.BE.entities.User;
import com.tobioxd.BE.payload.responses.LoginResponse;
import com.tobioxd.BE.payload.responses.UserResponse;
import com.tobioxd.BE.repositories.UserRepository;
import com.tobioxd.BE.services.impl.TokenServiceImpl;
import com.tobioxd.BE.services.impl.UserServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OAuth2UserCustomServiceImpl implements IOauth2UserCustomService {

        private final OAuth2AuthorizedClientService authorizedClientService;
        private final CustomOAuth2UserDetailService customOAuth2UserDetailService;
        private final UserRepository userRepository;
        private final UserServiceImpl userServiceImpl;
        private final TokenServiceImpl tokenServiceImpl;
        private final JwtTokenUtil jwtTokenUtil;

        @Override
        public User getUserInfo(HttpServletRequest request) {

                // Get the OAuth2AuthenticationToken
                OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder
                                .getContext()
                                .getAuthentication();

                // Get the OAuth2AuthorizedClient
                OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                                authentication.getAuthorizedClientRegistrationId(),
                                authentication.getName());

                // Get the atributes
                OAuth2User oAuth2User = authentication.getPrincipal();

                // Construct the OAuth2UserRequest
                ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
                OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
                OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest(clientRegistration, accessToken,
                                oAuth2User.getAttributes());

                customOAuth2UserDetailService.loadUser(oAuth2UserRequest);

                // Extract email from attributes
                String email = (String) oAuth2User.getAttributes().get("email");

                return userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Could not find user with email: " + email));
        }

        @Override
        public LoginResponse loginUser(HttpServletRequest request) throws Exception {

                User user = getUserInfo(request);

                String token = jwtTokenUtil.generateToken(user);

                User userDetail = userServiceImpl.getUserDetailsFromToken(token);
                Token jwtToken = tokenServiceImpl.addToken(userDetail, token);

                return LoginResponse.builder()
                                .message("Login successful")
                                .token(jwtToken.getToken())
                                .tokenType(jwtToken.getTokenType())
                                .refreshToken(jwtToken.getRefreshToken())
                                .user(UserResponse.fromUser(user))
                                .roles(user.getAuthorities().stream().map(item -> item.getAuthority()).toList())
                                .id(user.getId())
                                .build();
        }

}
