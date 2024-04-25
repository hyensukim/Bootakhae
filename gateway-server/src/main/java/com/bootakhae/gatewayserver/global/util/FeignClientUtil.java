package com.bootakhae.gatewayserver.global.util;

import com.bootakhae.gatewayserver.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//@Service
@RequiredArgsConstructor
public class FeignClientUtil {
    private final UserClient userClient;

    public void validateToken(String authorizationHeader) {
        userClient.validateAccessToken(authorizationHeader);
    }
}
