package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.order.Orders;
import com.supshop.suppingmall.page.TenItemsCriteria;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderMapper {

    void save(Orders orders);

    int findCount(String type,Long id);

    Optional<Orders> findOne(Long orderId);

    List<Orders> findAll(LocalDateTime fromDate, LocalDateTime toDate, String searchValue, TenItemsCriteria criteria);

    List<Orders> findByBuyerId(Long userId, LocalDateTime fromDate, LocalDateTime toDate, String type, String searchValue);

    List<Orders> findBySellerId(Long userId, LocalDateTime fromDate, LocalDateTime toDate, String type, String searchValue, TenItemsCriteria criteria);

    void updateOrder(Orders orders);

}
