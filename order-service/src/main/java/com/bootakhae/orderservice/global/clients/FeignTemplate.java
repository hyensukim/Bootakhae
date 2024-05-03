package com.bootakhae.orderservice.global.clients;

import com.bootakhae.orderservice.global.constant.StockProcess;
import com.bootakhae.orderservice.wishlist.vo.response.ResponseProduct;
import com.bootakhae.orderservice.wishlist.vo.response.ResponseUser;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeignTemplate {

    private final UserClient userClient;
    private final ProductClient productClient;

    /**
     * 회원 정보 조회
     */
    @Retry(name = "default-RT")
    public ResponseUser findUserByUserId(String userId) {
        return userClient.getUser(userId);
    }

    /**
     * 상품 정보 조회
     */
    @Retry(name = "default-RT")
    public ResponseProduct findProductByProductId(String productId) {
        return productClient.getOneProduct(productId);
    }

    /**
     * 상품 정보 일괄 조회
     */
    @Retry(name = "default-RT")
    public List<ResponseProduct> findProductsByProductIds(List<String> productIds) {
        return productClient.getProducts(productIds);
    }

    /**
     * 재고 업데이트
     */
    // fixme 이벤트 발생하여 재고 일치화 해주도록 개선!! - 이벤트 브로커
    @CircuitBreaker(name = "default-CB")
    public ResponseProduct updateStock(StockProcess process, String productId, Long qty) {
        if("DECREASE".equals(process.name())){
            return productClient.decreaseStock(productId, qty);
        }
        else {
            return productClient.restoreStock(productId, qty);
        }
    }
}
