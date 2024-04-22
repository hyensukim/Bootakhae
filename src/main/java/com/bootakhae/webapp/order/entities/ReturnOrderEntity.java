package com.bootakhae.webapp.order.entities;

import com.bootakhae.webapp.common.entities.BaseEntity;
import com.bootakhae.webapp.order.constant.Reason;
import com.bootakhae.webapp.order.dto.ReturnOrderDto;
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
