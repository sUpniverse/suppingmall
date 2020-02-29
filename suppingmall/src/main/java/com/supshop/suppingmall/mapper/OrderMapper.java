package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.order.Order;

import java.util.Optional;

public interface OrderMapper {
    
    void insertOrder(Order order);

    Optional<Order> findOne(Long orderId);
}
