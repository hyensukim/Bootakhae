package com.bootakhae.webapp.order.services;

import com.bootakhae.webapp.order.dto.OrderDto;
import com.bootakhae.webapp.order.dto.ReturnOrderDto;

import java.util.List;

public interface OrderService {

    OrderDto registerOrder(OrderDto orderDetails);

    OrderDto registerOrders(OrderDto orderDetails);

    OrderDto removeOrder(String orderId);

    OrderDto getOrderDetails(String orderId);

    List<OrderDto> getOrderListByUserId(String userId, int nowPage, int pageSize);

    OrderDto returnOrderedProduct(String orderId, ReturnOrderDto returnOrderDetails);

    void changeOrderStatus();

    OrderDto updateOrder(String orderId, OrderDto orderDetails);

}
