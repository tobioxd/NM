package com.tobioxd.BE.services.oauth2.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OAuth2UserDetailsCustom implements OAuth2User, UserDetails{

    private String id;

    private String phoneNumber;

    private String email;

    private String password;

    private String name;

    private String role;

    private String photoUrl;

    private String providerId;

    private Date createdAt;

    private boolean isActive;

    private String passwordResetToken;

    private Date passwordResetExpirationDate;

    private Date passwordChangeAt;

    private Map<String, Object> attributes;

    public OAuth2UserDetailsCustom(String id, String username, String name, String role) {
        this.id = id;
        this.email = username;
        this.name = name;
        this.role = role;
    }

    @Override
    public<A> A getAttribute(String name){
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_"+getRole().toUpperCase()));
        return authorityList;
    }

    @Override
    public String getUsername() {
        return email;
    }

    

}
