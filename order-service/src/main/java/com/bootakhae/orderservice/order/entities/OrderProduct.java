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
//                        String productName,
//                        Long productStock,
//                        Long price
                        Long qty
    ) {
        this.order = order;
        this.productId = productId;
//        this.productName = productName;
//        this.productStock = productStock;
//        this.price = price;
        this.qty = qty;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

//    public void registerOrder(OrderEntity order) { //    양방향 시, order 를 등록하기 위한 코드
//        this.order = order;
//    }

    @Column(name = "product_id", nullable = false, length = 50)
    private String productId;

    // todo : 재고 관련 로직 변경 후 삭제 예정
//    @Column(name = "order_product_name", nullable = false, length = 50)
//    private String productName;
//    @Column(name = "order_product_stock", nullable = false)
//    private Long productStock;
//    public long restoreStock(long qty){
//        productStock += qty;
//        return productStock;
//    } // 수량 증가
//
//    public long decreaseStock(long qty){
////    public synchronized long decreaseStock(long qty){
//        productStock -= qty;
//        return productStock;
//    } // 수량 감소

    // todo : 필요 없는 데이터 추후 삭제 예정
//    @Column(name = "order_product_price", nullable = false)
//    private Long price;

    @Column(name = "order_product_qty", nullable = false)
    private Long qty;

    public static OrderProduct createOrderedProduct(OrderEntity order, ResponseProduct product, Long qty) {
        return OrderProduct.builder()
                .order(order)
                .productId(product.getProductId())
//                .price(product.getPrice())
                .qty(qty)
                .build();
    }

    public OrderProductDto entityToDto() {
        return OrderProductDto.builder()
                .productId(this.productId)
//                .stock(this.productStock)
                .qty(this.qty)
//                .price(this.price)
                .build();
    }
}
