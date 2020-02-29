package com.supshop.suppingmall.order;

import com.supshop.suppingmall.mapper.OrderMapper;

import java.util.NoSuchElementException;
import java.util.Optional;

public class OrderService {

    private OrderMapper orderMapper;

    public Long createOrder(Order order) {
        orderMapper.insertOrder(order);
        return order.getOrderId();
    }

    public Order retrieveOrder(Long orderId) {
        Optional<Order> order = orderMapper.findOne(orderId);
        return order.orElseThrow(NoSuchElementException::new);
    }
}
