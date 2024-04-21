package com.bootakhae.webapp.order.services;

import com.bootakhae.webapp.order.dto.OrderDto;

public interface OrderService {

    OrderDto registerOrder(OrderDto orderDetails);

    OrderDto registerOrders(OrderDto orderDetails);

    String deleteOrder(String orderId);

    String getOrderDetails(String orderId);

    String updateOrder(String orderId, OrderDto orderDetails);

    String getOrderListByUserId(String userId);
}
