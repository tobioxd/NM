package com.tobioxd.BE.services.oauth2.security;

import java.util.Date;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.tobioxd.BE.entities.User;
import com.tobioxd.BE.exceptions.BaseException;
import com.tobioxd.BE.repositories.UserRepository;
import com.tobioxd.BE.services.oauth2.OAuth2UserDetails;
import com.tobioxd.BE.services.oauth2.OAuth2UserDetailsFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserDetailService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {

            return checkingOAuth2User(userRequest, user);

        } catch (OAuth2AuthenticationException e) {
            throw new BaseException("400", e.getMessage());
        } catch (Exception e) {
            throw new BaseException("400", e.getMessage());
        }

    }

    private OAuth2User checkingOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserDetails oAuth2UserDetails = OAuth2UserDetailsFactory
                .getOAuth2User(userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        Optional<User> user = userRepository.findByEmail(oAuth2UserDetails.getEmail());

        User userDetails;
        if (user.isPresent()) {
            userDetails = user.get();

            if(userDetails.getPhotoUrl().equals("default.jpg")){
                userDetails = updateExistingUser(userDetails, oAuth2UserDetails);
            }

        } else {
            userDetails = registerNewUser(userRequest, oAuth2UserDetails);
        }

        return new OAuth2UserDetailsCustom(userDetails.getId(), userDetails.getUsername(), userDetails.getPassword(), "user");

    }

    public User registerNewUser(OAuth2UserRequest userRequest, OAuth2UserDetails oAuth2UserDetails) {
        User user = new User();

        user.setEmail(oAuth2UserDetails.getEmail());
        user.setName(oAuth2UserDetails.getName());
        user.setRole("user");
        user.setActive(true);
        user.setPhotoUrl(oAuth2UserDetails.getPhotoUrl());

        user.setCreatedAt(new Date());

        return userRepository.save(user);
    }

    public User updateExistingUser(User existingUser, OAuth2UserDetails oAuth2UserDetails) {
        existingUser.setPhotoUrl(oAuth2UserDetails.getPhotoUrl());

        return userRepository.save(existingUser);
    }

    // private OAuth2UserRequest getUserInfo(HttpServletRequest request) {
        
    //     // Get the OAuth2AuthenticationToken
    //     OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

    //     // Get the OAuth2AuthorizedClient
    //     OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
    //             authentication.getAuthorizedClientRegistrationId(),
    //             authentication.getName()
    //     );

    //     // Get the atributes
    //     OAuth2User oAuth2User = authentication.getPrincipal();

    //     // Construct the OAuth2UserRequest
    //     ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
    //     OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
    //     OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest(clientRegistration, accessToken, oAuth2User.getAttributes());

    //     return oAuth2UserRequest;

    // }

}
