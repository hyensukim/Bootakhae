package com.bootakhae.webapp.order.entities;

import com.bootakhae.webapp.common.entities.BaseEntity;
import com.bootakhae.webapp.order.constant.Status;
import com.bootakhae.webapp.order.dto.OrderDto;
import com.bootakhae.webapp.order.dto.OrderProductDto;
import com.bootakhae.webapp.order.dto.ReturnOrderDto;
import com.bootakhae.webapp.user.entities.UserEntity;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class OrderEntity extends BaseEntity {

    @Builder
    public OrderEntity(String address1,
                       String address2,
                       String phone,
                       Long totalPrice,
                       UserEntity user){
        this.orderId = UUID.randomUUID().toString();
        this.address1 = address1;
        this.address2 = address2;
        this.phone = phone;
        this.totalPrice = totalPrice;
        this.user = user;
        this.status = Status.PAYMENT;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq; // PK

    @Column(name = "order_id", nullable = false, unique = true, length = 50)
    private String orderId; // 주문 아이디

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user; // 단방향 매핑

//    public void registerConsumer(UserEntity user){
//        this.user = user;
//    }
//    주문 내 어떤 상품이 들어있는지 조회시 양방향 매핑
    @OneToMany(mappedBy = "order")
    private final List<OrderProduct> orderProducts = new ArrayList<>(); // 양방향 매핑

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.registerOrder(this);
    }

    @Column(name = "order_address1", nullable = false, length = 100)
    private String address1; // 우편번호

    @Column(name = "order_address2", nullable = false, length = 100)
    private String address2; // 상세 주소

    @Column(name = "order_phone", nullable = false, length = 50)
    private String phone; // 연락처

    @Column(name = "order_total_price", nullable = false)
    private Long totalPrice; // 총 비용

    public void calculateTotalPrice(Long totalPrice){
        this.totalPrice = totalPrice;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 50)
    private Status status;

    public void startShipping(){ this.status = Status.SHIPPING; }

    public void completeShipping(){ this.status = Status.DONE; }

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
        List<OrderProductDto> orderedProducts = new ArrayList<>();
        ReturnOrderDto returnOrderDto = null;

        if(!orderProducts.isEmpty()){
            orderedProducts = this.orderProducts.stream().map(OrderProduct::entityToDto).toList();
        }

        // todo 수정 - 이 로직말고는 답이 없을까?
        if(returnOrder != null){
            returnOrderDto = returnOrder.entityToDto();
        }

        return OrderDto.builder()
                .orderId(this.orderId)
                .userId(this.user.getUserId())
                .totalPrice(this.totalPrice)
                .address1(this.address1)
                .address2(this.address2)
                .phone(this.phone)
                .createdAt(this.getCreatedAt())
                .orderStatus(this.status)
                .returnOrder(returnOrderDto)
                .orderedProducts(orderedProducts)
                .build();
    }
}
