package com.bootakhae.userservice.global.jwt;

import com.bootakhae.userservice.dto.UserDto;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenProvider tokenProvider;
    private final TokenService tokenService;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                TokenService tokenService,
                                TokenProvider tokenProvider) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
        this.tokenService = tokenService;
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
                            credential.getPassword(),
                            new ArrayList<>()
                    )
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

        String userName = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDetails = tokenProvider.getUserDetailsByEmail(userName);
        String accessToken = tokenProvider.createAccessToken(userDetails);
        String refreshToken = tokenProvider.createRefreshToken();

        tokenService.saveRefreshToken(userDetails.getUserId(), refreshToken);

        Cookie cookie = new Cookie("refresh-token", refreshToken);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setHttpOnly(true);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        response.addCookie(cookie);
        response.addHeader("userId",userDetails.getUserId());
    }
}
