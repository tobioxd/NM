package com.tobioxd.BE.services.oauth2;

import java.util.Map;

public abstract class OAuth2UserDetails {

    protected Map<String, Object> attributes;

    public OAuth2UserDetails(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getPhotoUrl();

}
