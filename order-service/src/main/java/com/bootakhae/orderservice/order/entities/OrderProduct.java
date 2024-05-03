package com.bootakhae.orderservice.order.entities;

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
                        String productName,
                        Long productStock,
                        Long qty,
                        Long price) {
        this.order = order;
        this.productId = productId;
        this.productName = productName;
        this.productStock = productStock;
        this.qty = qty;
        this.price = price;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    public void registerOrder(OrderEntity order) { //    양방향 시, order 를 등록하기 위한 코드
        this.order = order;
    }

    @Column(name = "product_id", nullable = false, length = 50)
    private String productId;

    @Column(name = "order_product_name", nullable = false, length = 50)
    private String productName;

    @Column(name = "order_product_stock", nullable = false)
    private Long productStock; // todo Product-Service 와 동기화 처리 필수!

    public long restoreStock(long qty){
        productStock += qty;
        return productStock;
    } // 수량 증가

    public long decreaseStock(long qty){
//    public synchronized long decreaseStock(long qty){
        productStock -= qty;
        return productStock;
    } // 수량 감소

    @Column(name = "order_product_qty", nullable = false)
    private Long qty;

    @Column(name = "order_product_price", nullable = false)
    private Long price;

    public OrderProductDto entityToDto() {
        return OrderProductDto.builder()
                .productId(this.productId)
                .stock(this.productStock)
                .qty(this.qty)
                .price(this.price)
                .build();
    }
}
