package com.bootakhae.gatewayserver.filters;

import com.bootakhae.gatewayserver.global.exception.ErrorCode;
import com.bootakhae.gatewayserver.global.exception.ErrorResponse;
import com.bootakhae.gatewayserver.global.util.RedisUtil;
import com.bootakhae.gatewayserver.global.util.TokenProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpHeaders;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    TokenProvider tokenProvider;

    public AuthorizationHeaderFilter(Environment env, TokenProvider tokenProvider) {
        super(Config.class);
        this.tokenProvider = tokenProvider;
    }
    
    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.debug("Authorization header filter started");
            ServerHttpRequest request= exchange.getRequest();
            
            // 1. 헤더에 포함된 JWT 정보 확인
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, ErrorResponse.of(ErrorCode.NOT_FOUND_ACCESS_TOKEN));
            }

            // 2. JWT 추출
            String jwt = tokenProvider.getToken(request);

            // 3. JWT 검증
            if(!tokenProvider.isValid(jwt)){
                if(tokenProvider.isExpired(jwt)){
                    log.debug("access-token 검증 : 토큰 만료");
                    return onError(exchange, ErrorResponse.of(ErrorCode.EXPIRED_ACCESS_TOKEN));
                }
                log.debug("access-token 검증 : 인증 실패");
                return onError(exchange, ErrorResponse.of(ErrorCode.INVALID_ACCESS_TOKEN));
            }

            log.debug("access-token 검증 : 인증 성공");
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, ErrorResponse errorResponse) {
        ServerHttpResponse response = exchange.getResponse();

        // 오류 상태코드 작성
        response.setStatusCode(errorResponse.getStatus());

        // 오류 응답 바디 작성
        try {
            byte[] errorByte = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .writeValueAsBytes(errorResponse);

            DataBuffer dataBuffer = response.bufferFactory().wrap(errorByte);
            return response.writeWith(Mono.just(dataBuffer));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return response.setComplete();
        }
    }
}
