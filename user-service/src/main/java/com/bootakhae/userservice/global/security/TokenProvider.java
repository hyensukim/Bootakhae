package com.bootakhae.userservice.global.security;

import com.bootakhae.userservice.dto.UserDto;
import com.bootakhae.userservice.services.UserService;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider{

    private final UserService userService;

    private final Environment env;

    public String createAccessToken(String userId, String roles){
        String expiredTime = Objects.requireNonNull(env.getProperty("token.access-expired-time"));
        return Jwts.builder()
                .subject(userId)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(expiredTime)*1000L))
                .signWith(getSigningKey())
                .compact();
    }

    public String createRefreshToken(String roles){
        String expiredTime = Objects.requireNonNull(env.getProperty("token.refresh-expired-time"));
        return Jwts.builder()
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(expiredTime)*1000L))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateRefreshToken(String token){
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        }catch (Exception e){
            log.info("Refresh Token 검증 : ",e);
            return false;
        }
    }

    public Date getExpiredTime(String token){
        return getClaimsFromToken(token).getExpiration();
    }

    public String getRolesByToken(String token){
        return getClaimsFromToken(token).get("roles", String.class);
    }

    public UserDto getUserDetailsByEmail(String email){
        return userService.getUserDetailsByEmail(email);
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("token.secret"));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
