package com.bootakhae.userservice.global.security;

import com.bootakhae.userservice.dto.UserDto;
import com.bootakhae.userservice.services.RefreshTokenService;
import com.bootakhae.userservice.vo.request.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

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
import java.util.stream.Collectors;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                TokenProvider tokenProvider,
                                RefreshTokenService refreshTokenService) {
        super(authenticationManager);
        this.refreshTokenService = refreshTokenService;
        this.tokenProvider = tokenProvider;

    }

    // 1
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLogin credential = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credential.getEmail(),
                            credential.getPassword())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 3
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.debug("로그인 성공 후 처리");

        UserDetails user = ((User) authResult.getPrincipal());

        List<String> roleList = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String roles = roleList.stream().collect(Collectors.joining(","));

        String email = user.getUsername();

        UserDto userDetails = tokenProvider.getUserDetailsByEmail(email);

        String accessToken = tokenProvider.createAccessToken(userDetails.getUserId(), roles);
        Date expiredTime = tokenProvider.getExpiredTime(accessToken);
        String refreshToken = tokenProvider.createRefreshToken(roles);

        refreshTokenService.saveTokenInfo(userDetails.getUserId(), refreshToken);

        response.setContentType("application/json");

        // body 설정
        Map<String, Object> tokens = Map.of(
                "userId", userDetails.getUserId(),
                "accessToken", accessToken,
                "expiredTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiredTime)
        );

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
