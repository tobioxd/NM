package com.tobioxd.BE.entities;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "be_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@SuppressWarnings("deprecation")
public class User implements UserDetails {

    @Id
    @Column(name = "id", nullable = false)
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    private String id;

    @Column(name = "phone_number", length = 255, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "role", length = 255, nullable = false)
    private String role;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_expiration_date")
    private Date passwordResetExpirationDate;

    @Column(name = "password_change_at")
    private Date passwordChangeAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_"+getRole().toUpperCase()));
        return authorityList;
    }
    
    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
