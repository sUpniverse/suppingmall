package com.supshop.suppingmall.order.form;

import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.payment.Payment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@ToString
public class OrderForm {

    private Long orderId;
    private Payment payment;
    private Delivery delivery;
}
