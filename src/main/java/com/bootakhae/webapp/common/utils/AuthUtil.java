package com.bootakhae.webapp.common.utils;

import com.bootakhae.webapp.user.entities.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
}
