package com.bootakhae.orderservice.order.services;

import com.bootakhae.orderservice.order.dto.OrderDto;
import com.bootakhae.orderservice.order.dto.PayDto;
import com.bootakhae.orderservice.order.dto.ReturnOrderDto;

import java.util.List;

public interface OrderService {

    OrderDto registerOrder(OrderDto orderDetails);

    void completePayment(PayDto payDetails);

    OrderDto cancelOrder(String orderId);

    OrderDto getOrderDetails(String orderId);

    List<OrderDto> getOrderListByUserId(String userId, int nowPage, int pageSize);

    void changeOrderStatusAfterPayment();

    void changeOrderStatusAfterShipping();

    void changeOrderStatusForReturn();

    OrderDto returnOrderedProduct(ReturnOrderDto returnOrderDetails);
}
