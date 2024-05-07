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

    public ProductDto decrease(String productId, Long qty) {
        log.debug("분산락 실행 - 재고 감소");

        RLock lock = redissonClient.getLock(productId);
        try{
            boolean available = lock.tryLock(100, 1, TimeUnit.SECONDS);

            if(!available){
                throw new CustomException(ErrorCode.LOCK_NOT_AVAILABLE);
            }

            return productService.decreaseStock(productId,qty);
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }finally{
            lock.unlock();
        }
    }

    public ProductDto restore(String productId, Long qty){
        log.debug("분산락 실행 - 재고 복구");

        RLock lock = redissonClient.getLock(productId);
        try{
            boolean available = lock.tryLock(100, 1, TimeUnit.SECONDS);

            if(!available){
                throw new CustomException(ErrorCode.LOCK_NOT_AVAILABLE);
            }

            return productService.restoreStock(productId,qty);
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }
}
