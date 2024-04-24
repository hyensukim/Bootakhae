package com.bootakhae.userservice.global.jwt;

import com.bootakhae.userservice.global.utils.AuthUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtValidationFilter extends OncePerRequestFilter {

    private  final AuthUtil authUtil;
    private  final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        log.debug("JWT 검증 필터 방문");
        String accessToken = authUtil.getToken(req);

        if(accessToken != null){ // access-token 이 있는가
            if(tokenProvider.validateToken(accessToken)){
                log.debug("access-token 검증 : 인증 성공");
                setAuthentication(accessToken);
            }
            else{
                log.debug("access-token 검증 : 인증 실패");
                if(tokenProvider.isExpired(accessToken)){
                    log.debug("access-token 검증 : 기한 만료");
                    resp.setContentType("text/plain; charset=UTF-8");
                    resp.getWriter().write("access-token 재발급 바랍니다.");
                }

                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        chain.doFilter(req,resp);
    }

    private void setAuthentication(String accessToken){
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
