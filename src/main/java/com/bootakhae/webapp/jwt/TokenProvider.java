package com.bootakhae.webapp.jwt;

import com.bootakhae.webapp.user.dto.UserDto;
import com.bootakhae.webapp.user.services.UserService;

import io.jsonwebtoken.Claims;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider{

    private  final Environment env;
    private  final UserService userService;

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
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
            return true;
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return false;
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("token.secret"));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
