package com.bootakhae.webapp.order.entities;

import com.bootakhae.webapp.order.dto.OrderProductDto;
import com.bootakhae.webapp.product.entities.ProductEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
                        ProductEntity product,
                        Long quantity,
                        Long price) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private OrderEntity order;

//    양방향 시, order 를 등록하기 위한 코드
    public void registerOrder(OrderEntity order) {
        this.order = order;
    }

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "order_product_qty", nullable = false)
    private Long quantity;

    @Column(name = "order_product_price", nullable = false)
    private Long price;

    public OrderProductDto entityToDto() {
        return OrderProductDto.builder()
                .productId(this.product.getProductId())
                .stock(this.product.getStock())
                .quantity(this.quantity)
                .price(this.price)
                .build();
    }
}
