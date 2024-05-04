package com.bootakhae.productservice.facade;

import com.bootakhae.productservice.global.redis.LettuceTemplate;
import com.bootakhae.productservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LettuceInventoryFacade {

    private final LettuceTemplate lettuceTemplate;

    private final ProductService productService;

    public void decrease(String productId, Long qty) throws InterruptedException{
        // InventoryService 의 decrease 연산 앞 뒤에 락을 걸고 해제하는 코드입니다.
        while(!lettuceTemplate.lock(productId)){
            Thread.sleep(100);
        }
        try{
            productService.decreaseStock(productId, qty);
        }finally{
            lettuceTemplate.unlock(productId);
        } // 예외와 상관 없이 Lock 반환
    }
}
