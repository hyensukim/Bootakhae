package com.bootakhae.orderservice.order.dto;

import com.bootakhae.orderservice.global.constant.Status;
import com.bootakhae.orderservice.order.entities.OrderEntity;
import com.bootakhae.orderservice.order.vo.response.ResponseOrder;
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
    private String address1;
    private String address2;
    private String phone;

    private List<OrderProductDto> orderProductList;
    private String payMethod;

    private String orderId;
    private String payId;
    private Long totalPrice;
    private Status orderStatus;
    private LocalDateTime returnAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ResponseOrder dtoToVo(){
        return ResponseOrder.builder()
                .orderId(this.orderId) // 주문번호
                .userId(this.userId) // 주문한 회원
                .payId(this.payId) // 결제번호
                .payMethod(this.payMethod) // 결제 방법
                .totalPrice(this.totalPrice) // 전체 비용
                .address1(this.address1) // 주소
                .address2(this.address2)
                .phone(this.phone) // 연락처
                .returnAt(this.returnAt) // 반품일자
                .createdAt(this.createdAt) // 주문일자
                .updatedAt(this.updatedAt) // 업데이트 일자
                .orderStatus(this.orderStatus) // 주문 상태
                .orderedProducts(this.orderProductList)// 주문 상품
                .build();
    }

    public OrderEntity dtoToEntity(){
        return OrderEntity.builder()
                .userId(this.userId)
                .address1(this.address1)
                .address2(this.address2)
                .phone(this.phone)
                .build();
    }
}
