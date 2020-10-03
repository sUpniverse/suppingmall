package com.supshop.suppingmall.order.form;

import com.supshop.suppingmall.order.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TempOrderForm {

    private Long orderId;
    private Long productId;
    private Long buyerId;
    private Long sellerId;
    private List<OrderItem> orderItems;

}
