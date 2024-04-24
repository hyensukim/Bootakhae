package com.bootakhae.userservice.global.utils;

import com.bootakhae.userservice.entities.UserEntity;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class AuthUtil {

    public UserEntity getUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            return (UserEntity) authentication.getPrincipal();
        }
        else{
            return null;
        }
    }

    public String getToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(authorizationHeader)){
            return authorizationHeader.substring(7);
        }else{
            return null;
        }
    }

    public String getRefreshToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            return cookies[0].getValue();
        }
        else{
            return null;
        }
    }
}
