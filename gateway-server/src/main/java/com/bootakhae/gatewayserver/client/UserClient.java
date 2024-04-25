package com.bootakhae.gatewayserver.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service") // Eureka-Server 등록 네이밍
public interface UserClient {

    @GetMapping("api/v1/users/token")
    void validateAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);
}
