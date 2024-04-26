package com.bootakhae.orderservice.clients;

import com.bootakhae.orderservice.wishlist.vo.response.ResponseUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("api/v1/internal/users/{userId}")
    ResponseUser getUser(@PathVariable("userId") String userId);
}
