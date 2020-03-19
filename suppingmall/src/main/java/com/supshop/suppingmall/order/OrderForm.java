package com.supshop.suppingmall.order;

import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.payment.Payment;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderForm {

    private Long orderId;
    private Payment payment;
    private Delivery delivery;
}
