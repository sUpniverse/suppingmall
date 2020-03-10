package com.supshop.suppingmall.order;

import com.supshop.suppingmall.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderService {

    private OrderMapper orderMapper;

    public Long createOrder(Orders orders) {
        orderMapper.insertOrder(orders);
        return orders.getOrderId();
    }

    public Orders retrieveOrder(Long orderId) {
        Optional<Orders> order = orderMapper.findOne(orderId);
        return order.orElseThrow(NoSuchElementException::new);
    }
}
