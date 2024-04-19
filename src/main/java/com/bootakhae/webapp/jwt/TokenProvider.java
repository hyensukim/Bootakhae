package com.bootakhae.webapp.jwt;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider{

    private  final Environment env;
    private  UserService userService;

// 해당 로직 AuthenticationFilter 안으로 이전
//    public String createToken(String userId){
//        /*권한이 여러개인 경우 구현*/
//        String authorities = auth.getAuthorities()
//                .stream().map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//        long millis = System.currentTimeMillis();
//        Date now = new Date(millis);
//        Date expiration = new Date(millis + Long.parseLong(Objects.requireNonNull(
//                env.getProperty("token.expiration_time"))));
//
//        return Jwts.builder()
//                .subject(userId)
//                .issuedAt(now)
//                .expiration(expiration)
//                .claim("auth", authorities) // 권한이 여러개인 경우 구현
//                .signWith(getSigningKey())
//                .compact();
//    }

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

        UserDetails userDetails = userService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
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
