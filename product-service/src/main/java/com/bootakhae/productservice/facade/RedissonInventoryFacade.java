package com.bootakhae.productservice.facade;

import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.global.exception.CustomException;
import com.bootakhae.productservice.global.exception.ErrorCode;
import com.bootakhae.productservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonInventoryFacade {
    private final RedissonClient redissonClient;

    private final ProductService productService;

    /**
     * 테스트 용
     */
    public void decreaseStock(String productId, Long amount) {

        RLock lock = redissonClient.getLock(productId);

        try{
            boolean available = lock.tryLock(2, 3, TimeUnit.SECONDS);

            if(!available){
                throw new CustomException(ErrorCode.LOCK_NOT_AVAILABLE);
            }

            productService.decreaseStockTest(productId, amount);
        }
        catch(InterruptedException e){
            throw new RuntimeException(e);
        }finally{
            lock.unlock();
        }
    }
}
