package com.bootakhae.userservice.global.security;

import com.bootakhae.userservice.global.exception.ErrorCode;
import com.bootakhae.userservice.global.utils.AuthUtil;
import com.bootakhae.userservice.global.utils.ErrorMessageUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class JwtValidationFilter extends OncePerRequestFilter {
//
//    private final ErrorMessageUtil errorMessageUtil;
//    private final AuthUtil authUtil;
//    private final TokenProvider tokenProvider;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
//            throws ServletException, IOException {
//        log.debug("JWT 검증 필터 방문");
//        String accessToken = authUtil.getToken(req);
//
//        if(accessToken != null){ // access-token 이 있는가
//            if(tokenProvider.validateToken(accessToken)){
//                log.debug("access-token 검증 : 인증 성공");
//                setAuthentication(accessToken);
//            }
//            else{
//                if(tokenProvider.isExpired(accessToken)){
//                    log.debug("access-token 검증 : 기한 만료");
//                    resp.setContentType("application/json; charset=UTF-8");
//                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED
//                            ,errorMessageUtil.writeMessage(ErrorCode.EXPIRED_ACCESS_TOKEN));
//                    return;
//                }
//                else{
//                    log.debug("access-token 검증 : 인증 실패");
//                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//                    return;
//                }
//            }
//        }
//        chain.doFilter(req,resp);
//    }
//
//    private void setAuthentication(String accessToken){
//        Authentication authentication = tokenProvider.getAuthentication(accessToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//}
