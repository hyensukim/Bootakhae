package com.bootakhae.gatewayserver.filters;

import com.bootakhae.gatewayserver.exception.ErrorCode;
import com.bootakhae.gatewayserver.exception.ErrorResponse;
import com.bootakhae.gatewayserver.util.TokenProvider;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdminAccessFilter  extends AbstractGatewayFilterFactory<AdminAccessFilter.Config> {

    TokenProvider tokenProvider;

    public AdminAccessFilter(Environment env, TokenProvider tokenProvider) {
        super(AdminAccessFilter.Config.class);
        this.tokenProvider = tokenProvider;
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(AdminAccessFilter.Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {

            String jwt = tokenProvider.getToken(exchange.getRequest());

            log.debug("access-token 검증 : 권한 확인");
            String roles = tokenProvider.getRolesByToken(jwt);
            if (Objects.isNull(roles) || !roles.contains("ADMIN")) {
                return ErrorResponse.onError(exchange, ErrorResponse.of(ErrorCode.NOT_ACCESSIBLE_AUTHORITY));
            }

            return chain.filter(exchange);
        },2);
    }
}