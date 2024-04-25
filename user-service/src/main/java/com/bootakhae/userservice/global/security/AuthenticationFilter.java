package com.bootakhae.userservice.global.security;

import com.bootakhae.userservice.dto.UserDto;
import com.bootakhae.userservice.global.utils.CookieUtil;
import com.bootakhae.userservice.services.TokenService;
import com.bootakhae.userservice.vo.request.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;
    private final TokenService tokenService;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                TokenService tokenService,
                                TokenProvider tokenProvider, CookieUtil cookieUtil) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
        this.tokenService = tokenService;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        // credential - 자격증명, getInputStream()을 통해서 요청 바디에 담긴 데이터를 처리할 수 있다!
        try {
            // 1. 요청 데이터 받기
            RequestLogin credential = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            // 2. Spring Security 에서 사용 가능한 인증 정보로 변환 - UsernamePasswordAuthenticationToken 으로 변환
            // 3. AuthenticationManager 에게 인증 요청 정보 넘기기
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credential.getEmail(),
                            credential.getPassword())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.debug("로그인 성공 후 처리");

        UserDetails user = ((User) authResult.getPrincipal());

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String email = user.getUsername();
        UserDto userDetails = tokenProvider.getUserDetailsByEmail(email);

        String accessToken = tokenProvider.createAccessToken(userDetails,roles);
        Date expiredTime = tokenProvider.getExpiredTime(accessToken);
        String refreshToken = tokenProvider.createRefreshToken();

        tokenService.saveRefreshToken(userDetails.getUserId(), refreshToken);

        ResponseCookie responseCookie = cookieUtil.createRefreshTokenCookie(refreshToken);
        Cookie cookie = cookieUtil.of(responseCookie);

        response.setContentType("application/json");
        response.addCookie(cookie);

        // body 설정
        Map<String, Object> tokens = Map.of(
                "accessToken", accessToken,
                "expiredTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiredTime)
        );

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
