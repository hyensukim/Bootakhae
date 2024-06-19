package com.bootakhae.gatewayserver.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final Environment env;

    /**
     * 토큰 추출
     */
    public String getToken(ServerHttpRequest request){
        String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(authorizationHeader)){
            return authorizationHeader.substring(7);
        }else{
            return null;
        }
    }

    /**
     * 토큰 검증
     */
    public boolean isValid(String token) {
        try{
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            return false;
        }catch(Exception e){
            log.error("token 검증 중 오류 발생 : {}",e.getMessage());
            return false;
        }
    }

    /**
     * 토큰 만료 여부 확인
     */
    public boolean isExpired(String token){
        try{
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return false;
        }catch(ExpiredJwtException e){
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public String getUserIdByToken(String token){
        return getClaimsFromToken(token).getSubject();
    }

    public String getRolesByToken(String token){
        return getClaimsFromToken(token).get("roles", String.class);
    }

    /**
     * 토큰 claims 가져오기
     */
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("jwt.secretKey"));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
