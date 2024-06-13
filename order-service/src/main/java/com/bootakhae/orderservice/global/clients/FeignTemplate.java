package com.bootakhae.orderservice.global.clients;

import com.bootakhae.orderservice.global.clients.vo.request.RequestStock;
import com.bootakhae.orderservice.global.clients.vo.response.ResponsePay;
import com.bootakhae.orderservice.global.clients.vo.response.ResponseProduct;
import com.bootakhae.orderservice.global.clients.vo.response.ResponseUser;
import com.bootakhae.orderservice.global.exception.ServerException;
import com.bootakhae.orderservice.order.dto.OrderProductDto;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeignTemplate {

    private final ProductClient productClient;
    private final UserClient userClient;
    private final PayClient payClient;

    public ResponseUser findUserByUserId(String userId){
        return userClient.getUser(userId);
    }

    /**
     * 재고 수정
     */
    public List<ResponseProduct> updateStock(RequestStock request) {
        return productClient.updateStock(request);
    }

    /**
     * 재고 감소
     */
    public List<ResponseProduct> checkAndDecreaseStock(List<OrderProductDto> orderProductList){
        return productClient.checkAndDecreaseStock(orderProductList.stream()
                .map(OrderProductDto::dtoToVo)
                .collect(Collectors.toList())
        );
    }
    /**
     * 재고 복구
     */
    public void restoreStock(List<OrderProductDto> orderProductList){
        productClient.restoreStock(orderProductList.stream()
                .map(OrderProductDto::dtoToVo)
                .collect(Collectors.toList())
        );
    }

    /**
     * 결제 정보 조회
     */
//    @Retry(name="default-RT", fallbackMethod = "fallbackForPay")
    @CircuitBreaker(name = "default-CB", fallbackMethod = "fallbackForPay")
    public ResponsePay getOnePay(String payId){
        return payClient.getOnePay(payId);
    }

    private ResponsePay fallbackForPay(String payId, ServerException e){
        log.debug("ServerException fallback : {}", e.getMessage());
        return makeResponsePay(payId);
    }

    private ResponsePay fallbackForPay(String payId, CallNotPermittedException e){
        log.debug("CallNotPermittedException fallback : {}", e.getMessage());
        return makeResponsePay(payId);
    }

    private ResponsePay fallbackForPay(String payId, RetryableException e){
        log.debug("RetryableException fallback : {}", e.getMessage());
        return makeResponsePay(payId);
    }

    private ResponsePay fallbackForPay(String payId, IOException e){
        log.debug("IOException fallback : {}", e.getMessage());
        return makeResponsePay(payId);
    }

    private ResponsePay fallbackForPay(String payId, TimeoutException e){
        log.debug("TimeoutException fallback : {}", e.getMessage());
        return makeResponsePay(payId);
    }

    private ResponsePay fallbackForPay(String payId, HttpServerErrorException e){
        log.debug("HttpServerErrorException fallback : {}", e.getMessage());
        return makeResponsePay(payId);
    }

    private ResponsePay makeResponsePay(String payId){
        return ResponsePay.builder()
                .payId(payId)
                .payMethod(null)
                .totalPrice(null)
                .build();
    }
}
