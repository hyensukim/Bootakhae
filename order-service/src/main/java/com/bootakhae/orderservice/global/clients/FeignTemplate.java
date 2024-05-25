package com.bootakhae.orderservice.global.clients;

import com.bootakhae.orderservice.global.clients.vo.request.RequestPay;
import com.bootakhae.orderservice.global.clients.vo.request.RequestStock;
import com.bootakhae.orderservice.global.clients.vo.response.ResponsePay;
import com.bootakhae.orderservice.global.constant.StockProcess;
import com.bootakhae.orderservice.global.clients.vo.response.ResponseProduct;
import com.bootakhae.orderservice.global.clients.vo.response.ResponseUser;
import com.bootakhae.orderservice.order.dto.OrderProductDto;
import com.bootakhae.orderservice.order.vo.ProductInfo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
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
     * 결제 생성
     */
//    public ResponsePay registerPay(RequestPay request){
//        return payClient.payment(request);
//    }

    /**
     * 결제 정보 조회
     */
    @Retry(name="default-RT", fallbackMethod = "fallbackForPay")
//    @CircuitBreaker(name = "default-CB", fallbackMethod = "fallbackForPay")
    public ResponsePay getOnePay(String payId){
        return payClient.getOnePay(payId);
    }

    private ResponsePay fallbackForPay(String payId, Throwable throwable){
        log.debug("getOnePay fallback : {}", throwable.getMessage());
        return ResponsePay.builder()
                .payId(payId)
                .payMethod("ERR")
                .totalPrice(0L)
                .build();
    }
}
