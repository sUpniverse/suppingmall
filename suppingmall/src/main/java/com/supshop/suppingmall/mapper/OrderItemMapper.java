package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.order.OrderItem;
import com.supshop.suppingmall.order.Orders;
import com.supshop.suppingmall.page.TenItemsCriteria;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderItemMapper {

    void saveList(List<OrderItem> orderItems);

    int findCount(String type,Long id);

    OrderItem findOne(Long orderId);

    List<OrderItem> findAll(LocalDateTime fromDate, LocalDateTime toDate, String searchValue, TenItemsCriteria criteria);

    List<OrderItem> findByBuyerId(Long userId, LocalDateTime fromDate, LocalDateTime toDate, String type, String searchValue);

    List<OrderItem> findBySellerId(Long userId, LocalDateTime fromDate, LocalDateTime toDate, String type, String searchValue, TenItemsCriteria criteria);

    void updateList(List<OrderItem> orderItems);

    void updateStatus(OrderItem orderItem);
}
