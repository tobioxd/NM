package com.tobioxd.BE.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.tobioxd.BE.entities.Token;
import com.tobioxd.BE.entities.User;
import com.tobioxd.BE.exceptions.InvalidParamException;
import com.tobioxd.BE.repositories.TokenRepository;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("deprecation")
@Component
@RequiredArgsConstructor

public class JwtTokenUtil {

    @Value("${jwt.expiration}")
    private int expiration; // save to an environment variable

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    @Value("${jwt.secretKey}")
    private String secretKey;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    private final TokenRepository tokenRepository;

    public String generateToken(User user) throws Exception {

        // properties => claims
        Map<String, Object> claims = new HashMap<>();

        // this.generateSecretKey();
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("userId", user.getId());
        
        try {
            String token = Jwts.builder()
                    .setClaims(claims) // how to extract claims from this ?
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {
            throw new InvalidParamException("Cannot create jwt token, error: " + e.getMessage());
        }
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // check expiration
    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String phoneNumber = extractPhoneNumber(token);
            Token existingToken = tokenRepository.findByToken(token);
            if (existingToken == null || existingToken.isRevoked() == true) {
                return false;
            }
            return (phoneNumber.equals(userDetails.getUsername()))
                    && !isTokenExpired(token);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}