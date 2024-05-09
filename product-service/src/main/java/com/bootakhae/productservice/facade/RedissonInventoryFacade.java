package com.bootakhae.productservice.facade;

import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.global.exception.CustomException;
import com.bootakhae.productservice.global.exception.ErrorCode;
import com.bootakhae.productservice.services.ProductService;
import com.bootakhae.productservice.vo.request.ProductInfo;
import com.bootakhae.productservice.vo.request.RequestStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonInventoryFacade {
    private final RedissonClient redissonClient;

    private final ProductService productService;

    /**
     * 첫 항만 Lock
     */
    public List<ProductDto> update(RequestStock request){
        log.debug("분산락 실행 - 재고 수량 변경 실행 : {}", request.getStockProcess());

        List<ProductInfo> productInfoList = request.getProductInfoList();

        ProductInfo firstInfo = productInfoList.getFirst();
        RLock lock = redissonClient.getLock(firstInfo.getProductId());

        try{
            boolean available = lock.tryLock(100, 1, TimeUnit.SECONDS);

            if(!available){
                throw new CustomException(ErrorCode.LOCK_NOT_AVAILABLE);
            }

           return productService.updateStock(request);
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 각 항목마다 Lock
     */
    public List<ProductDto> updateList(RequestStock request){
        log.debug("각 항목 분산락 실행 - 재고 수량 변경 실행 : {}", request.getStockProcess());

        List<ProductInfo> productInfoList = request.getProductInfoList();
        Map<String, RLock> locks = new LinkedHashMap<>(); // 상품 ID와 해당 락을 매핑하기 위한 Map

        try {
            // 모든 상품에 대해 락을 시도합니다.
            for (ProductInfo info : productInfoList) {
                RLock lock = redissonClient.getLock(info.getProductId());
                boolean available = lock.tryLock(100, 10, TimeUnit.SECONDS); // 락 획득 시도

                if (!available) { // 락 획득 실패 시
                    throw new CustomException(ErrorCode.LOCK_NOT_AVAILABLE);
                }

                locks.put(info.getProductId(), lock); // 성공적으로 락을 획득했으면 Map에 추가
            }

            // 모든 상품에 대한 락 획득 후 재고 업데이트 처리
            return productService.updateStock(request);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 현재 스레드의 interrupt 상태를 설정
            throw new RuntimeException(e);
        } finally {
            // 모든 락을 해제합니다.
            for (RLock lock : locks.values()) {
                if (lock.isHeldByCurrentThread()) { // 현재 스레드가 락을 보유하고 있는지 확인
                    lock.unlock();
                }
            }
        }
    }
}
