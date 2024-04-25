package com.bootakhae.userservice.global.security;

import com.bootakhae.userservice.dto.UserDto;
import com.bootakhae.userservice.services.UserService;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider{

    private final Environment env;
    private final UserService userService;

    @Value("${token.access-expired-time}")
    private String ACCESS_EXPIRED_TIME;

    @Value("${token.refresh-expired-time}")
    private String REFRESH_EXPIRED_TIME;

    @Value("${token.secret}")
    private String SECRET;

    public String createAccessToken(UserDto userDetails, List<String> roles){
        return Jwts.builder()
                .subject(userDetails.getUserId())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(ACCESS_EXPIRED_TIME)*1000L))
                .signWith(getSigningKey())
                .compact();
    }

    public String createRefreshToken(){
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(REFRESH_EXPIRED_TIME)*1000L))
                .signWith(getSigningKey())
                .compact();
    }

    public Date getExpiredTime(String token){
        return getClaimsFromToken(token).getExpiration();
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
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    public Authentication getAuthentication(String token) {
//        Claims claims = Jwts.parser()
//                .verifyWith(getSigningKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
        /* 권한이 여러개인 경우에, 해당 로직으로 구현 */
//        String authorities = String.valueOf(claims.get("auth"));
//        authorities = StringUtils.hasText(authorities) ? authorities : Role.USER.name();
//        List<SimpleGrantedAuthority> authorityList = Arrays.stream(authorities.split(","))
//                .map(SimpleGrantedAuthority::new).toList();
//
//        UserDto dto = userService.getOneByUserId(claims.getSubject());
//
//        List<SimpleGrantedAuthority> authorityList = Arrays.stream(dto.getRole().name().split(","))
//                .map(SimpleGrantedAuthority::new).toList();
//
//        User user =  new User(dto.getUserId(),dto.getPassword(),
//                true,true,true,true,
//                authorityList);
//
//        return new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
//    }

//    public boolean validateToken(String token){
//        try{
//            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
//            return true;
//        } catch (SignatureException e) {
//            log.error("Invalid JWT signature: {}", e.getMessage());
//            return false;
//        } catch (MalformedJwtException e) {
//            log.error("Invalid JWT token: {}", e.getMessage());
//            return false;
//        } catch (ExpiredJwtException e) {
//            log.error("JWT token is expired: {}", e.getMessage());
//            return false;
//        } catch (UnsupportedJwtException e) {
//            log.error("JWT token is unsupported: {}", e.getMessage());
//            return false;
//        } catch (IllegalArgumentException e) {
//            log.error("JWT claims string is empty: {}", e.getMessage());
//            return false;
//        }catch(Exception e){
//            log.error("token 검증 중 오류 발생 : {}",e.getMessage());
//            return false;
//        }
//    }

//    public boolean isExpired(String token){
//        try{
//            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
//            return false;
//        }
//        catch(ExpiredJwtException e){
//            log.debug("Access token 만료됐습니다.",e);
//            return true;
//        }
//    }
}
