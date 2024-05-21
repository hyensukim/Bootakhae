package com.bootakhae.gatewayserver.filters;

import com.bootakhae.gatewayserver.exception.ErrorCode;
import com.bootakhae.gatewayserver.exception.ErrorResponse;
import com.bootakhae.gatewayserver.util.TokenProvider;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpHeaders;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> implements Ordered {

    TokenProvider tokenProvider;

    public AuthorizationHeaderFilter(Environment env, TokenProvider tokenProvider) {
        super(Config.class);
        this.tokenProvider = tokenProvider;
    }

    @Override
    public int getOrder() {
        return 1;
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request= exchange.getRequest();

            // 1. 헤더에 포함된 JWT 정보 확인
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return ErrorResponse.onError(exchange, ErrorResponse.of(ErrorCode.NOT_FOUND_ACCESS_TOKEN));
            }

            // 2. JWT 추출
            String jwt = tokenProvider.getToken(request);

            // 3. JWT 검증
            if(!tokenProvider.isValid(jwt)){
                if(tokenProvider.isExpired(jwt)){
                    log.debug("access-token 검증 : 토큰 만료");
                    return ErrorResponse.onError(exchange, ErrorResponse.of(ErrorCode.EXPIRED_ACCESS_TOKEN));
                }
                log.debug("access-token 검증 : 인증 실패");
                return ErrorResponse.onError(exchange, ErrorResponse.of(ErrorCode.INVALID_ACCESS_TOKEN));
            }

            log.debug("access-token 검증 : 인증 성공");
            return chain.filter(exchange);
        };
    }
}
