package com.bootakhae.userservice.global.utils;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final Environment env;

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        String expiredTime = Objects.requireNonNull(env.getProperty("token.refresh-expired-time"));
        return ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Long.parseLong(expiredTime)).build();
    }

    public ResponseCookie removeRefreshTokenCookie() {
        return ResponseCookie.from("refresh-token")
                .maxAge(0)
                .path("/")
                .build();
    }

    public Cookie of(ResponseCookie responseCookie) {
        Cookie cookie = new Cookie(responseCookie.getName(), responseCookie.getValue());
        cookie.setPath(responseCookie.getPath());
        cookie.setSecure(responseCookie.isSecure());
        cookie.setHttpOnly(responseCookie.isHttpOnly());
        cookie.setMaxAge((int) responseCookie.getMaxAge().getSeconds());
        return cookie;
    }
}
