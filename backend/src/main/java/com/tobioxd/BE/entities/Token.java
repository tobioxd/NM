package com.tobioxd.BE.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "be_tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@SuppressWarnings("deprecation")
public class Token {

    @Id
    @Column(name = "id", nullable = false)
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    private String id;

    @Column(name = "token", length = 255)
    private String token;

    @Column(name = "refresh_token", length = 255)
    private String refreshToken;

    @Column(name = "token_type", length = 50)
    private String tokenType;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "refresh_expiration_date")
    private Date refreshExpirationDate;

    private boolean revoked;
    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
