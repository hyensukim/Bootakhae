package com.bootakhae.orderservice.dto;

import com.bootakhae.orderservice.global.constant.Status;
import com.bootakhae.orderservice.vo.response.ResponseOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    // 요청 들어온 정보
    private String userId;
    private String productId;
    private String address1;
    private String address2;
    private String phone;
    private Long quantity;
//    private Boolean chooseBasicAddress;

    private Long unitPrice;

    private String orderId;
    private Long totalPrice;
    private Status orderStatus;
    private LocalDateTime createdAt;

    private ReturnOrderDto returnOrder;
    private OrderProductDto orderProduct;
    private List<OrderProductDto> orderedProducts;

    public ResponseOrder dtoToVo(){
        return ResponseOrder.builder()
                .orderId(this.orderId) // 주문번호
                .userId(this.userId) // 주문한 회원
                .totalPrice(this.totalPrice) // 전체 비용
                .address1(this.address1) // 주소
                .address2(this.address2)
                .phone(this.phone) // 연락처
                .createdAt(this.createdAt) // 주문일자
                .orderStatus(this.orderStatus)
                .returnOrder(this.returnOrder)
                .orderedProduct(this.orderProduct)// 주문 상태
                .orderedProducts(this.orderedProducts) // 주문 상품
                .build();
    }
}
