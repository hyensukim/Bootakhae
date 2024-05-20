package com.bootakhae.orderservice.order.entities;

import com.bootakhae.orderservice.global.clients.vo.response.ResponseProduct;
import com.bootakhae.orderservice.order.dto.OrderProductDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_product")
@Getter
@NoArgsConstructor
public class OrderProduct {

    @Builder
    public OrderProduct(OrderEntity order,
                        String productId,
                        Long qty
    ) {
        this.order = order;
        this.productId = productId;
        this.qty = qty;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(name = "product_id", nullable = false, length = 50)
    private String productId;

    @Column(name = "order_product_qty", nullable = false)
    private Long qty;

    public static OrderProduct createOrderedProduct(OrderEntity order,String productId, Long qty) {
        return OrderProduct.builder()
                .order(order)
                .productId(productId)
                .qty(qty)
                .build();
    }

    public OrderProductDto entityToDto() {
        return OrderProductDto.builder()
                .productId(this.productId)
                .qty(this.qty)
                .build();
    }
}
