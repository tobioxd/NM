package com.tobioxd.BE.services.oauth2;

import java.util.Map;

public class OAuth2FacebookUser extends OAuth2UserDetails {

    public OAuth2FacebookUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getPhotoUrl() {
        return "default.jpg";
    }
    
}
