package com.bootakhae.orderservice.global.clients;

import com.bootakhae.orderservice.global.clients.vo.response.ResponseUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("api/v1/internal/users/{userId}")
    ResponseUser getUser(@PathVariable("userId") String userId);

    /* TEST */
    @GetMapping("test/internal/errorful/case1")
    String case1();

    /* TEST */
    @GetMapping("test/internal/errorful/case2")
    String case2();

    /* TEST */
    @GetMapping("test/internal/errorful/case3")
    String case3();
}
