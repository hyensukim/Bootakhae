package com.bootakhae.payservice.entities;

import com.bootakhae.payservice.dto.PayDto;
import com.bootakhae.payservice.global.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name="pays")
@Getter
@NoArgsConstructor
public class PayEntity extends BaseEntity {

    @Builder
    public PayEntity(
            String orderId,
            String payMethod,
            Long totalPrice
    ){
        this.payId = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.payMethod = PayMethod.valueOf(payMethod);
        this.totalPrice = totalPrice;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name="pay_id", nullable=false, length = 50)
    private String payId;

    @Column(name="order_id", nullable=false, length = 50)
    private String orderId;

    @Enumerated(EnumType.STRING)
    @Column(name="pay_method", nullable=false, length = 50)
    private PayMethod payMethod;
    enum PayMethod {
        CARD("신용/체크 카드"),
        VIRTUAL_ACCOUNT("가상 계좌"),
        TRANSFER("계좌 이체"),
        MOBILE("휴대폰")
        ;

        final String description;

        PayMethod(String description){
            this.description = description;
        }
    }

    @Column(name="pay_total_price", nullable=false)
    private Long totalPrice;

    public PayDto entityToDto(){
        return PayDto.builder()
                .payId(this.payId)
                .orderId(this.orderId)
                .payMethod(this.payMethod.name())
                .totalPrice(this.totalPrice)
                .createdAt(this.getCreatedAt())
                .build();
    }
}
