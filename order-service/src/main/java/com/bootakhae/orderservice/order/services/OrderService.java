package com.bootakhae.orderservice.order.services;

import com.bootakhae.orderservice.order.dto.OrderDto;
import com.bootakhae.orderservice.order.dto.ReturnOrderDto;

import java.util.List;

public interface OrderService {

    OrderDto registerOrder(OrderDto orderDetails);

    OrderDto registerOrders(OrderDto orderDetails);

    OrderDto removeOrder(String orderId);

    OrderDto getOrderDetails(String orderId);

    List<OrderDto> getOrderListByUserId(String userId, int nowPage, int pageSize);

    OrderDto returnOrderedProduct(ReturnOrderDto returnOrderDetails);
}
