package com.supshop.suppingmall.order;

import com.supshop.suppingmall.mapper.OrderItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemMapper orderItemMapper;

    public OrderItem getOrderItem(Long orderItemId) {

        return orderItemMapper.findOne(orderItemId);
    }

    public void saveList(List<OrderItem> orderItems) {
        orderItemMapper.saveList(orderItems);
    }

    public void updateOrderItemList(List<OrderItem> orderItems) {
        orderItemMapper.updateList(orderItems);
    }
}
