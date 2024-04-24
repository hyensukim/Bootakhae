package com.bootakhae.orderservice.entities;


import com.bootakhae.orderservice.global.constant.Reason;
import com.bootakhae.orderservice.dto.ReturnOrderDto;
import com.bootakhae.orderservice.global.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "return_orders")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @OneToOne(mappedBy = "returnOrder")
    private OrderEntity order;

    @Enumerated(EnumType.STRING)
    @Column(name = "reutrn_reason")
    private Reason reason;

    public void writeReason(Reason reason){
        this.reason = reason;
    }

    public ReturnOrderDto entityToDto(){
        return ReturnOrderDto.builder()
                .reason(this.reason)
                .build();
    }
}
