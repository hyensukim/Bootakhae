package com.bootakhae.productservice.service;

import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.entities.ProductEntity;
import com.bootakhae.productservice.facade.LettuceInventoryFacade;
import com.bootakhae.productservice.facade.OptimisticInventoryFacade;
import com.bootakhae.productservice.facade.RedissonInventoryFacade;
import com.bootakhae.productservice.repositories.ProductRepository;
import com.bootakhae.productservice.services.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 통합 테스트
 * 스프링 컨테이너를 생성하여 테스트 코드가 실행됩니다.
 * 만약, 단위 테스트를 진행하고 싶은 경우 Mock 객체를 만들어서 수행합니다.
 * 단점 : 테스트에 필요없는 빈까지 함께 등록되므로, 시간이 오래 걸립니다. - 슬라이스 테스트!(Mockito)
 * 
 * 단위 테스트 - 하나의 빈으로 테스트
 * 통합 테스트 - 여러 빈을 등록하여 전체적이 기능을 수행하는 테스트
 *
 * 빈이란 ? 스프링 빈 컨테이너의 제어를 받는 객체를 총칭하는 명칭입니다.
 */
@SpringBootTest
public class InventoryTest {

    // 자동 주입이 되지 않기 떄문에 일일히 @Autowired 를 사용하여 주입을 해줘야 합니다.
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private OptimisticInventoryFacade optimisticInventoryFacade;

    @Autowired
    private LettuceInventoryFacade lettuceInventoryFacade;

    @Autowired
    private RedissonInventoryFacade redissonInventoryFacade;

    private static final String uuid = UUID.randomUUID().toString();

    @BeforeEach // 실제 테스트를 돌리기 전에 아이템 1개, 1번 ID 부여, 100개 재고로 집어넣기
    public void insert(){
        ProductEntity product = ProductEntity.builder()
                .productId(uuid)
                .seq(1L)
                .name("텐텐")
                .producer("우리집")
                .price(1000L)
                .stock(10000L)
//                .version(1L)
                .nutritionFacts("단백질 100%")
                .build();

        productRepository.save(product);
    }

    @AfterEach // 테스트 수행 후 디비 초기화
    public void delete(){
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("100개의 재고를 가진 1번 아이템을 1개 감소시키면 99개가 남는다.")
    public void 동시성문제가생기지않는재고감소상황(){
        // given

        //when
        ProductDto productDto = productService.decreaseStock(uuid,1L );

        //then
        assertEquals(99, productDto.getStock());
    }

    @Test
    @DisplayName("멀티스레드를 활용해서 동시에 5000명이 1개씩 주문을 넣는 상황")
    public void 동시에5000명이주문을하는상황() throws InterruptedException{
        //given
        final int threadCount = 5000; // 동시 요청 갯수
        ExecutorService executorService = Executors.newFixedThreadPool(30); // 30개의 쓰레드
        CountDownLatch countDownLatch = new CountDownLatch(threadCount); // 요청 마친 쓰레드는 대기하도록 처리

        //when
        for(int i=0; i< 5000 ;i++){
            executorService.submit(() -> {
                try{
                    ProductDto dto = productService.decreaseStock(uuid, 1L);
//                    System.out.println(dto.getStock());
                }finally{
                    countDownLatch.countDown(); // 요청이 들어간 쓰레드는 대기 상태로 전환
                }
            });
        }
        countDownLatch.await(); // 모든 쓰레드의 호출이 끝나면 쓰레드 풀 자체 종료

        ProductEntity product = productRepository.findByProductId(uuid).orElseThrow();

        //then
        assertEquals(0, product.getStock());
    }

    @Test
    @DisplayName("Pessimistic Lock 을 통한 동시성 제어")
    public void 동시에5000명이주문하는상황비관적() throws InterruptedException{
        //given
        final int threadCount = 10000; // 동시 요청 갯수
        ExecutorService executorService = Executors.newFixedThreadPool(10000); // 30개의 쓰레드
        CountDownLatch countDownLatch = new CountDownLatch(threadCount); // 요청 마친 쓰레드는 대기하도록 처리

        //when
        for(int i=0; i< 10000 ;i++){
            executorService.submit(() -> {
                try{
                    ProductDto dto = productService.decreaseStockPessimistic(uuid, 1L);
                }finally{
                    countDownLatch.countDown(); // 요청이 들어간 쓰레드는 대기 상태로 전환
                }
            });
        }
        countDownLatch.await(); // 모든 쓰레드의 호출이 끝나면 쓰레드 풀 자체 종료

        ProductEntity product = productRepository.findByProductId(uuid).orElseThrow();

        //then
        assertEquals(0, product.getStock());
    }

    @Test
    @DisplayName("Optimistic Lock 을 통한 동시성 제어")
    public void 동시에5000명이주문하는상황낙관적() throws InterruptedException{
        //given
        final int threadCount = 20000; // 동시 요청 갯수
        ExecutorService executorService = Executors.newFixedThreadPool(30); // 30개의 쓰레드
        CountDownLatch countDownLatch = new CountDownLatch(threadCount); // 요청 마친 쓰레드는 대기하도록 처리

        //when
        for(int i=0; i< 20000 ;i++){
            executorService.submit(() -> {
                try{
                    optimisticInventoryFacade.decrease(uuid, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally{
                    countDownLatch.countDown(); // 요청이 들어간 쓰레드는 대기 상태로 전환
                }
            });
        }
        countDownLatch.await(); // 모든 쓰레드의 호출이 끝나면 쓰레드 풀 자체 종료

        ProductEntity product = productRepository.findByProductId(uuid).orElseThrow();

        //then
        assertEquals(0, product.getStock());
    }

    @Test
    @DisplayName("Lettuce Distributed Lock 을 통한 동시성 제어")
    public void 동시에5000명이주문하는상황Lettuce() throws InterruptedException{
        //given
        final int threadCount = 5000; // 동시 요청 갯수
        ExecutorService executorService = Executors.newFixedThreadPool(30); // 30개의 쓰레드
        CountDownLatch countDownLatch = new CountDownLatch(threadCount); // 요청 마친 쓰레드는 대기하도록 처리

        //when
        for(int i=0; i< threadCount ;i++){
            executorService.submit(() -> {
                try{
                    lettuceInventoryFacade.decrease(uuid, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally{
                    countDownLatch.countDown(); // 요청이 들어간 쓰레드는 대기 상태로 전환
                }
            });
        }
        countDownLatch.await(); // 모든 쓰레드의 호출이 끝나면 쓰레드 풀 자체 종료

        ProductEntity product = productRepository.findByProductId(uuid).orElseThrow();

        //then
        assertEquals(0, product.getStock());
    }

    @Test
    @DisplayName("Redisson Distributed Lock 을 통한 동시성 제어")
    public void 동시에5000명이주문하는상황Redisson() throws InterruptedException{
        //given
        final int threadCount = 10000; // 동시 요청 갯수
        ExecutorService executorService = Executors.newFixedThreadPool(10000); // 30개의 쓰레드
        CountDownLatch countDownLatch = new CountDownLatch(threadCount); // 요청 마친 쓰레드는 대기하도록 처리

        //when
        for(int i=0; i< threadCount ;i++){
            executorService.submit(() -> {
                try{
                    redissonInventoryFacade.decrease(uuid, 1L);
                } finally{
                    countDownLatch.countDown(); // 요청이 들어간 쓰레드는 대기 상태로 전환
                }
            });
        }
        countDownLatch.await(); // 모든 쓰레드의 호출이 끝나면 쓰레드 풀 자체 종료

        ProductEntity product = productRepository.findByProductId(uuid).orElseThrow();

        //then
        assertEquals(0, product.getStock());
    }
}
