package com.bootakhae.payservice.entities;

import com.bootakhae.payservice.dto.PayDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class PayEntity {

    @Builder
    public PayEntity(
            String orderId,
            String payMethod
    ){
        this.payId = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.payMethod = PayMethod.valueOf(payMethod);
        this.status = Status.PAYING;
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
        CARD("신용 카드/체크 카드"),
        VIRTUAL_ACCOUNT("가상 계좌"),
        TRANSFER("계좌 이체"),
        MOBILE("휴대폰")
        ;

        final String description;

        PayMethod(String description){
            this.description = description;
        }
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_status", nullable = false, length = 50)
    private Status status;

    public void completePayment(){this.status = Status.DONE;}

    enum Status {
        PAYING("결제 중"),
        DONE("결제 완료");

        final String description;

        Status(String description){
            this.description = description;
        }
    }

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public PayDto entityToDto(){
        return PayDto.builder()
                .payId(this.payId)
                .payMethod(this.payMethod.name())
                .status(this.status.name())
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
