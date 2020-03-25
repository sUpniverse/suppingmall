package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.order.Orders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderMapper {

    void save(Orders orders);

    Optional<Orders> findOne(Long orderId);

    void order(Long orderId,Orders.OrderStatus status,Long deliveryId, Long paymentId);

    List<Orders> findAll(LocalDateTime fromDate, LocalDateTime toDate);

    List<Orders> findByBuyerId(Long userId, LocalDateTime fromDate, LocalDateTime toDate, String type, String searchValue);

    List<Orders> findBySellerId(Long userId, LocalDateTime fromDate, LocalDateTime toDate, String type, String searchValue);
}
