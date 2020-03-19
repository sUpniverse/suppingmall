package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.order.Orders;

import java.util.Optional;

public interface OrderMapper {
    
    void save(Orders orders);

    Optional<Orders> findOne(Long orderId);

    void order(Long orderId,Orders.OrderStatus status,Long deliveryId, Long paymentId);
}
