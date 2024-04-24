package com.bootakhae.userservice.global.jwt;

import com.bootakhae.userservice.dto.UserDto;
import com.bootakhae.userservice.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private static final Long REFRESH_EXPIRED_TIME = 30 * 60 * 1000L; // 1sec -> 24hrs


    public String createAccessToken(UserDto userDetails){
        return Jwts.builder()
                .subject(userDetails.getUserId())
                .expiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(Objects.requireNonNull(env.getProperty("token.expiration_time")))))
                .signWith(getSigningKey())
                .compact();
    }

    public String createRefreshToken(){
        return Jwts.builder()
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRED_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Username 을 기반으로 UserEntity 조회 후 있으면, UsernamePasswordAuthenticationToken 을 생성
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        /* 권한이 여러개인 경우에, 해당 로직으로 구현 */
//        String authorities = String.valueOf(claims.get("auth"));
//        authorities = StringUtils.hasText(authorities) ? authorities : Role.USER.name();
//        List<SimpleGrantedAuthority> authorityList = Arrays.stream(authorities.split(","))
//                .map(SimpleGrantedAuthority::new).toList();

        UserDto dto = userService.getOneByUserId(claims.getSubject());

        List<SimpleGrantedAuthority> authorityList = Arrays.stream(dto.getRole().name().split(","))
                .map(SimpleGrantedAuthority::new).toList();

        User user =  new User(dto.getUserId(),dto.getPassword(),
                true,true,true,true,
                authorityList);

        return new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        }
        catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean isExpired(String token){
        try{
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return false;
        }
        catch(ExpiredJwtException e){
            log.debug("Access token 만료됐습니다.",e);
            return true;
        }
    }

    public UserDto getUserDetailsByEmail(String email){
        return userService.getUserDetailsByEmail(email);
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("token.secret"));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
