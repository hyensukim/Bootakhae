package com.bootakhae.orderservice.service;

import com.bootakhae.orderservice.order.entities.OrderEntity;
import com.bootakhae.orderservice.order.entities.OrderProduct;
import com.bootakhae.orderservice.order.repositories.OrderProductRepository;
import com.bootakhae.orderservice.order.repositories.OrderRepository;
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
 */
@SpringBootTest
public class InventoryTest {

    // 자동 주입이 되지 않기 떄문에 일일히 @Autowired 를 사용하여 주입을 해줘야 합니다.
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    private static final String uuid = UUID.randomUUID().toString();

    @BeforeEach // 실제 테스트를 돌리기 전에 아이템 1개, 1번 ID 부여, 100개 재고로 집어넣기
    public void insert(){
        OrderEntity order = OrderEntity.builder()
                .orderId(uuid)
                .userId(UUID.randomUUID().toString())
                .phone("01012341234")
                .address1("주소1")
                .address2("주소2")
                .totalPrice(100000L)
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .order(order)
                .productId(UUID.randomUUID().toString())
                .productName("마약")
                .productStock(100L)
                .price(1000L)
                .qty(1L)
                .build();

        order.getOrderProducts().add(orderProduct);
        orderRepository.save(order);
    }

    @AfterEach // 테스트 수행 후 디비 초기화
    public void delete(){
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("100개의 재고를 가진 1번 아이템을 1개 감소시키면 99개가 남는다.")
    public void 동시성문제가생기지않는재고감소상황(){
        // given
        OrderEntity order = orderRepository.findByOrderId(uuid).orElseThrow();
        OrderProduct orderedProduct = orderProductRepository.findByOrder(order).orElseThrow();

        //when
        orderedProduct.decreaseStock(1L);

        //then
        assertEquals(99, orderedProduct.getProductStock());
    }

    @Test
    @DisplayName("멀티스레드를 활용해서 동시에 100명이 1개씩 주문을 넣는 상황")
    public void 동시에100명이주문을하는상황() throws InterruptedException{
        final int threadCount = 100; // 동시 요청 갯수
        ExecutorService executorService = Executors.newFixedThreadPool(30); // 300개의 쓰레드
        CountDownLatch countDownLatch = new CountDownLatch(threadCount); // 요청 마친 쓰레드는 대기하도록 처리

        // given
        OrderEntity order = orderRepository.findByOrderId(uuid).orElseThrow();

        for(int i=0; i< 100 ;i++){ //
            executorService.submit(() -> {
                try{
                    OrderProduct orderedProduct = orderProductRepository.findByProductIdPessimistic(order).orElseThrow();
                    orderedProduct.decreaseStock(1L);
                    orderProductRepository.save(orderedProduct);
                }finally{
                    countDownLatch.countDown(); // 요청이 들어간 쓰레드는 대기 상태로 전환
                }
            });
        }
        countDownLatch.await(); // 모든 쓰레드의 호출이 끝나면 쓰레드 풀 자체 종료

        Long stock = orderProductRepository.findByOrder(order).orElseThrow().getProductStock();

        assertEquals(0, stock);
    }
}
