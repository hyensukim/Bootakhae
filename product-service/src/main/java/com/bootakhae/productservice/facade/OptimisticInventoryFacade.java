package com.bootakhae.productservice.facade;

import com.bootakhae.productservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimisticInventoryFacade {
    /**
     * 퍼사드 클래스의 역할은 낙관적 락 서비스의 decrease 를 반영될 떄까지 지속적으로 재시도하는 로직을 Service 객체에 wrapping 하는 것
     */
    private final ProductService productService;

    public void decrease(String productId, Long qty) throws InterruptedException {
        while(true){ // 성공 할 때까지 무한 반복
            try{
                productService.decreaseStockOptimistic(productId, qty); // 서비스의 decrease 호출
                break; // 위 서비스 구문의 감소가 버전 정보가 맞아서 잘 처리될 경우, 반복문을 탈출
            }catch(Exception e){
                Thread.sleep(100);// 낙관적 락에 의해서 버전 정합성이 맞지 않아서 예외가 발생하게될 경우, 0.1초 대기 후 재시도.
            }
        }
    }
}
