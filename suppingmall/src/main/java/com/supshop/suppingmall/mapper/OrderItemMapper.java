package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.order.OrderItem;

import java.util.List;

public interface OrderItemMapper {

    void saveList(List<OrderItem> orderItems);

}
