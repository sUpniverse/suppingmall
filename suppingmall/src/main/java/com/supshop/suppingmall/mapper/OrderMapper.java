package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.order.Orders;
import com.supshop.suppingmall.page.OrderCriteria;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderMapper {

    void save(Orders orders);

    int findCount(String type,Long id);

    Optional<Orders> findOne(Long orderId);

    List<Orders> findAll(LocalDateTime fromDate, LocalDateTime toDate, String searchValue, OrderCriteria criteria);

    List<Orders> findByBuyerId(Long userId, LocalDateTime fromDate, LocalDateTime toDate, String type, String searchValue);

    List<Orders> findBySellerId(Long userId, LocalDateTime fromDate, LocalDateTime toDate, String type, String searchValue, OrderCriteria criteria);

    /*
    patch 형태의 update임 (duty 체킹 후 바뀐 포인트만 업데이트)
    Todo : 추후 캡슐화진행
    */
    void updateOrder(Long orderId,Orders.OrderStatus status,Long deliveryId, Long paymentId);

    Orders findOneByDeliveryId(Long deliveryId);

}
