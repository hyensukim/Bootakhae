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
     * 테스트 용
     */
    public void decreaseStock(String productId, Long amount) {

        RLock lock = redissonClient.getLock(productId);

        try{
            boolean available = lock.tryLock(100, 1, TimeUnit.SECONDS);

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
            // 모든 상품에 대해 락을 시도
            for (ProductInfo info : productInfoList) {
                RLock lock = redissonClient.getLock(info.getProductId());
                boolean available = lock.tryLock(100, 10, TimeUnit.SECONDS);

                if (!available) { // 락 획득 실패 시
                    throw new CustomException(ErrorCode.LOCK_NOT_AVAILABLE);
                }

                locks.put(info.getProductId(), lock);
            }
            return productService.updateStock(request);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 현재 스레드의 interrupt 상태를 설정
            throw new RuntimeException(e);
        } finally {
            for (RLock lock : locks.values()) {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * Lock을 세분화하여 적용하기
     */
//    public List<ProductDto> updateProducts(RequestStock request){
//
//        List<ProductInfo> productInfoList = request.getProductInfoList();
//
//        ProductInfo firstInfo = productInfoList.getFirst();
//        RLock lock = redissonClient.getLock(firstInfo.getProductId());
//
//        try{
//            boolean available = lock.tryLock(100, 1, TimeUnit.SECONDS);
//
//            if(!available){
//                throw new CustomException(ErrorCode.LOCK_NOT_AVAILABLE);
//            }
//
//            return productService.updateStock(request);
//        }catch(InterruptedException e){
//            throw new RuntimeException(e);
//        }finally {
//            lock.unlock();
//        }
//    }
}
