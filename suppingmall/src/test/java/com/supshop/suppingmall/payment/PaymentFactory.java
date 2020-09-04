package com.supshop.suppingmall.payment;

import com.supshop.suppingmall.order.Orders;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@Component
@ActiveProfiles("test")
public class PaymentFactory {

    public Payment buildPayment(Orders order) {

        return Payment.builder()
                .paymentType(Payment.PayGroupType.CARD)
                .price(order.getAmountPrice())
                .status(Payment.PaymentStatus.COMPLETE)
                .payDate(LocalDateTime.now())
                .vendorCheckNumber("vaf00")
                .build();
    }
}
