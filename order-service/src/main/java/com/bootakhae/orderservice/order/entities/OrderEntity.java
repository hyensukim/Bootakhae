package com.bootakhae.orderservice.order.entities;

import com.bootakhae.orderservice.global.constant.Status;
import com.bootakhae.orderservice.order.dto.OrderDto;
import com.bootakhae.orderservice.order.dto.OrderProductDto;
import com.bootakhae.orderservice.order.dto.ReturnOrderDto;
import com.bootakhae.orderservice.global.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class OrderEntity extends BaseEntity {

    @Builder
    public OrderEntity(String orderId,
                       String userId,
                       String address1,
                       String address2,
                       String phone
//                       ,Long totalPrice
    ){
        this.orderId = Objects.requireNonNullElse(orderId, UUID.randomUUID().toString());
        this.userId = userId;
        this.address1 = address1;
        this.address2 = address2;
        this.phone = phone;
//        this.totalPrice = Objects.requireNonNullElse(totalPrice, 0L);
        this.status = Status.PAYING;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq; // PK

    @Column(name = "order_id", nullable = false, unique = true, length = 50)
    private String orderId; // 주문 아이디

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "pay_id", length = 50)
    private String payId;
    public void registerPay(String payId){
        this.payId = payId;
    }

//    주문 내 어떤 상품이 들어있는지 조회시 양방향 매핑
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private final List<OrderProduct> orderProducts = new ArrayList<>(); // 양방향 매핑

//    public void addOrderProduct(OrderProduct orderProduct) {
//        orderProducts.add(orderProduct);
//        orderProduct.registerOrder(this);
//    }

    @Column(name = "order_address1", nullable = false, length = 100)
    private String address1; // 우편번호

    @Column(name = "order_address2", nullable = false, length = 100)
    private String address2; // 상세 주소

    @Column(name = "order_phone", nullable = false, length = 50)
    private String phone; // 연락처

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 50)
    private Status status;

    public void completePayment(){
        this.status = Status.PAYMENT;
    }

    public void startShipping(){
        this.status = Status.SHIPPING;
    }

    public void completeShipping(){
        this.status = Status.DONE;
    }

    public void cancelTheOrder(){
        this.status = Status.CANCEL;
    }

    public void returnTheOrder(){
        this.status = Status.RETURN;
    }

    @OneToOne
    @JoinColumn(name = "return_order_id")
    private ReturnOrderEntity returnOrder;
    public void returnOrder(ReturnOrderEntity returnOrder){
        this.returnOrder = returnOrder;
    }

    public OrderDto entityToDto(){
        return entityToDto(null, null);
    }

    public OrderDto entityToDto(Long totalPrice, String payMethod){
        List<OrderProductDto> orderedProducts = this.orderProducts
                .stream()
                .map(OrderProduct::entityToDto)
                .collect(Collectors.toList());

        return OrderDto.builder()
                .orderId(this.orderId)
                .userId(this.userId)
                .payId(this.payId)
                .payMethod(payMethod)
                .totalPrice(totalPrice)
                .address1(this.address1)
                .address2(this.address2)
                .phone(this.phone)
                .returnAt(this.returnOrder != null ? this.returnOrder.getCreatedAt() : null)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .orderStatus(this.status)
                .orderProductList(orderedProducts)
                .build();
    }
}
